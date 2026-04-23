/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.service.codegen.generator;


import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.teleapi.TeleMethod;
import colesico.framework.teleapi.dataport.DataPort;
import colesico.framework.teleapi.TeleFacade;
import com.palantir.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;

import static colesico.framework.teleapi.TeleFacade.TARGET_PROV_FIELD;

/**
 * Generate tele-facade class
 */
public class TeleFacadesGenerator {

    public static final String PARAM_SUFFIX = "Param";
    public static final String TARGET_INSTANCE_VAR = "target";
    public static final String RESULT_VAR = "result";
    protected final Logger logger = LoggerFactory.getLogger(TeleFacadesGenerator.class);

    protected final ServiceProcessorContext context;
    protected final VarNameSequence varNames = new VarNameSequence();

    protected final TeleBatchesGenerator batchesGenerator;

    public TeleFacadesGenerator(ServiceProcessorContext context) {
        this.context = context;
        this.batchesGenerator = new TeleBatchesGenerator(context.getProcessingEnv());
    }

    protected void generateConstructor(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleFacade.parentService().originClass().getOriginType())),
                TARGET_PROV_FIELD,
                Modifier.FINAL);

        mb.addStatement("super($N)", TARGET_PROV_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected CodeBlock generateBatches(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        for (TeleBatchElement batch : teleMethod.batches().values()) {
            if (batch.readContext() == null || batch.readContext().getCreationCode() == null) {
                throw CodegenException.of()
                        .message("Batch read context code not defined")
                        .element(teleMethod.serviceMethod().originMethod())
                        .build();
            }
            // Read batch: BatchType batch = dataPort.read()
            cb.add("\n// Read batch \n");
            cb.add("final $T $N = $N.$N(",
                    ClassName.bestGuess(batch.batchClassName()),
                    batch.batchVarName(),
                    TeleMethod.DATA_PORT_PARAM, DataPort.READ_METHOD);
            cb.add(batch.readContext().getCreationCode());
            cb.add(");\n");
        }
        return cb.build();
    }

    protected CodeBlock generateParamRetrieving(TeleInputElement parameter, CodeBlock.Builder invokerBuilder) {

        // ==== For simple param

        if (parameter instanceof TeleParameterElement) {
            CodeBlock ctx = ((TeleParameterElement) parameter).readContext().getCreationCode();
            // dataPot.read(new Context(...));
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N(", TeleMethod.DATA_PORT_PARAM, DataPort.READ_METHOD);
            cb.add(ctx);
            cb.add(")");
            return cb.build();
        }

        // ==== For batch filed param

        if (parameter instanceof TeleBatchFieldElement) {
            TeleBatchFieldElement batchParam = (TeleBatchFieldElement) parameter;
            // batch.getFiled();
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N()", batchParam.parentBatch().batchVarName(), batchParam.getterName());
            return cb.build();
        }

        throw CodegenException.of().message("Unsupported tele parameter element: " + parameter).build();
    }

    protected CodeBlock generateInvocation(TeleMethodElement teleMethod) {
        MethodElement originMethod = teleMethod.serviceMethod().originMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        // ============= Generate Batches retrieving from data port
        cb.add(generateBatches(teleMethod));

        // ============= Generate params retrieving from data port
        ArrayCodegen serviceMethodArgs = new ArrayCodegen();
        for (TeleInputElement param : teleMethod.parameters()) {
            CodeBlock value = generateParamRetrieving(param, cb);
            String paramName = param.originElement().getName() + PARAM_SUFFIX;
            serviceMethodArgs.add("$N", paramName);
            cb.add("\n// Assign tele-method parameter value from remote client or batch\n");
            cb.add("final $T $N = ", TypeName.get(param.originElement().getOriginType()), paramName);
            cb.add(value);
            cb.add(";\n");
        }

        TypeMirror returnType = originMethod.getReturnType();
        boolean voidResult = returnType instanceof NoType;

        //======================== Get service instance and call service method

        // Get service instance:  TargetType target = targetProvider.get();
        ServiceElement service = teleMethod.parentTeleFacade().parentService();
        TypeName serviceTypeName = TypeName.get(service.originClass().getOriginType());
        cb.addStatement("final $T $N = $N.get()", serviceTypeName,
                TARGET_INSTANCE_VAR,
                TeleFacade.TARGET_PROV_FIELD);

        // Create writer variable
        //   ResultType result =
        CodeBlock.Builder callMethodCb = CodeBlock.builder();
        callMethodCb.add("\n// Invoke target service method\n");
        if (!voidResult) {
            callMethodCb.add("final $T $N = ", TypeName.get(returnType), RESULT_VAR);
        }

        // Call service method
        //   target.method(...);
        callMethodCb.add(TARGET_INSTANCE_VAR + "." + teleMethod.serviceMethod().name()
                + "(" + serviceMethodArgs.toFormat() + ");\n", serviceMethodArgs.toValues());
        cb.add(callMethodCb.build());

        // Send result to client via data port dataPort.write(result,new Context());
        if (!voidResult) {
            cb.add("\n// Send result to remote client\n");
            cb.add("$N.$N($N,", TeleMethod.DATA_PORT_PARAM, DataPort.WRITE_METHOD, RESULT_VAR);
            CodeBlock writeCtx = teleMethod.writingContext().getCreationCode();
            cb.add(writeCtx);
            cb.add(");\n");
        }


        return cb.build();
    }

    protected void generateTeleMethodBuilders(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        for (TeleMethodElement teleMethod : teleFacade.teleMethods()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(teleMethod.builderName());
            methodBuilder.addJavadoc("Returns  $T<R,W> instance for target method '$N'",
                    ClassName.get(TeleMethod.class),
                    teleMethod.serviceMethod().name());
            methodBuilder.addModifiers(Modifier.PRIVATE);
            methodBuilder.returns(
                    ParameterizedTypeName.get(ClassName.get(TeleMethod.class),
                            ClassName.get(teleFacade.readContextClass()),
                            ClassName.get(teleFacade.writeContextClass())
                    )
            );

            // Subroutine definition
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("return ($N) -> {\n", TeleMethod.DATA_PORT_PARAM);
            cb.indent();
            cb.add(generateInvocation(teleMethod));
            cb.unindent();
            cb.add("};\n");

            methodBuilder.addCode(cb.build());
            classBuilder.addMethod(methodBuilder.build());
        }
    }

    protected void generateDescriptorsMethod(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleFacade.DESCRIPTORS_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(teleFacade.descriptorsClass()));
        mb.addCode(teleFacade.descriptorsMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createTeleFacade(ServiceElement service, TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = service.originClass().getPackageName();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, service.originClass().unwrap());
    }

    public void generate(ServiceElement service) {
        TeleFacadeElement teleFacade = service.teleFacade();
        if (teleFacade == null) {
            return;
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(teleFacade.facadeClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Service: " + service.originClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(TeleFacade.class),
                TypeName.get(service.originClass().getOriginType()),
                ClassName.get(teleFacade.descriptorsClass())));

        generateConstructor(teleFacade, classBuilder);
        generateTeleMethodBuilders(teleFacade, classBuilder);
        generateDescriptorsMethod(teleFacade, classBuilder);

        createTeleFacade(service, teleFacade, classBuilder);

        batchesGenerator.generate(teleFacade.batchPack());
    }

}
