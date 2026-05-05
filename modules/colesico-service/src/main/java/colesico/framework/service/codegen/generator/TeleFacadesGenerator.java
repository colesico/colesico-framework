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
import colesico.framework.teleapi.TeleCommand;
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
        this.batchesGenerator = new TeleBatchesGenerator(context.processingEnv());
    }

    protected void generateConstructor(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleFacade.parentService().originClass().originType())),
                TARGET_PROV_FIELD,
                Modifier.FINAL);

        mb.addStatement("super($N)", TARGET_PROV_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected CodeBlock generateBatches(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        for (TeleBatchElement batch : teleCommand.batches().values()) {
            if (batch.readContext() == null || batch.readContext().creationCode() == null) {
                throw CodegenException.of()
                        .message("Batch read context code not defined")
                        .element(teleCommand.serviceMethod().originMethod())
                        .build();
            }
            // Read batch: BatchType batch = dataPort.read()
            cb.add("\n// Read batch \n");
            cb.add("final $T $N = $N.$N(",
                    ClassName.bestGuess(batch.batchClassName()),
                    batch.batchVarName(),
                    TeleCommand.DATA_PORT_PARAM, DataPort.READ_METHOD);
            cb.add(batch.readContext().creationCode());
            cb.add(");\n");
        }
        return cb.build();
    }

    protected CodeBlock generateParamRetrieving(TeleInputElement parameter, CodeBlock.Builder invokerBuilder) {

        // ==== For simple param ================

        if (parameter instanceof TeleParameterElement) {
            CodeBlock ctx = ((TeleParameterElement) parameter).readContext().creationCode();
            // dataPot.read(new Context(...));
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N(", TeleCommand.DATA_PORT_PARAM, DataPort.READ_METHOD);
            cb.add(ctx);
            cb.add(")");
            return cb.build();
        }

        // ==== For batch filed param =============

        if (parameter instanceof TeleBatchFieldElement) {
            TeleBatchFieldElement batchParam = (TeleBatchFieldElement) parameter;
            // batch.getFiled();
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N()", batchParam.parentBatch().batchVarName(), batchParam.getterName());
            return cb.build();
        }

        // ======= Injected parameter ============

        if (parameter instanceof TeleInjectedParameterElement) {
            CodeBlock.Builder cb = CodeBlock.builder();
            // Actual values will be injected at service method interception layer
            cb.add("null");
            return cb.build();
        }

        throw CodegenException.of().message("Unsupported tele parameter element: " + parameter).build();
    }

    protected CodeBlock generateInvocation(TeleCommandElement teleCommand) {
        MethodElement originMethod = teleCommand.serviceMethod().originMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        // ============= Generate Batches retrieving from data port
        cb.add(generateBatches(teleCommand));

        // ============= Generate params retrieving (default from data port)
        ArrayCodegen serviceMethodArgs = new ArrayCodegen();
        for (TeleInputElement param : teleCommand.parameters()) {
            CodeBlock value = generateParamRetrieving(param, cb);
            String paramName = param.originElement().name() + PARAM_SUFFIX;
            serviceMethodArgs.add("$N", paramName);
            cb.add("\n// Assign tele-command parameter value from remote client or batch\n");
            cb.add("final $T $N = ", TypeName.get(param.originElement().originType()), paramName);
            cb.add(value);
            cb.add(";\n");
        }

        TypeMirror returnType = originMethod.returnType();
        boolean voidResult = returnType instanceof NoType;

        //======================== Get service instance and call service method

        // Get service instance:  TargetType target = targetProvider.get();
        ServiceElement service = teleCommand.parentTeleFacade().parentService();
        TypeName serviceTypeName = TypeName.get(service.originClass().originType());
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
        callMethodCb.add(TARGET_INSTANCE_VAR + "." + teleCommand.serviceMethod().name()
                + "(" + serviceMethodArgs.toFormat() + ");\n", serviceMethodArgs.toValues());
        cb.add(callMethodCb.build());

        // Send result to client via data port dataPort.write(result,new Context());
        if (!voidResult) {
            cb.add("\n// Send result to remote client\n");
            cb.add("$N.$N($N, ", TeleCommand.DATA_PORT_PARAM, DataPort.WRITE_METHOD, RESULT_VAR);
            CodeBlock writeCtx = teleCommand.writingContext().creationCode();
            cb.add(writeCtx);
            cb.add(");\n");
        }


        return cb.build();
    }

    protected void generateTeleCommandsMethods(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        for (TeleCommandElement teleCommand : teleFacade.teleCommands()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(teleCommand.commandMethodName());
            methodBuilder.addJavadoc("Tele-command method for target method '$N'", teleCommand.serviceMethod().name());
            methodBuilder.addModifiers(Modifier.PUBLIC);
            methodBuilder.returns(TypeName.VOID);

            // Param DataPort<R,W> dataPort
            methodBuilder.addParameter(
                    ParameterizedTypeName.get(ClassName.get(DataPort.class),
                            ClassName.get(teleFacade.readContextClass()),
                            ClassName.get(teleFacade.writeContextClass())
                    ), TeleCommand.DATA_PORT_PARAM
            );

            methodBuilder.addCode(generateInvocation(teleCommand));
            classBuilder.addMethod(methodBuilder.build());
        }
    }

    protected void generateCommandsMethod(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleFacade.COMMANDS_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(teleFacade.commandsClass()));
        mb.addCode(teleFacade.commandsMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createTeleFacade(ServiceElement service, TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = service.originClass().packageName();
        CodegenUtils.createJavaFile(context.processingEnv(), typeSpec, packageName, service.originClass().unwrap());
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
                TypeName.get(service.originClass().originType()),
                ClassName.get(teleFacade.commandsClass())));

        generateConstructor(teleFacade, classBuilder);
        generateTeleCommandsMethods(teleFacade, classBuilder);
        generateCommandsMethod(teleFacade, classBuilder);

        createTeleFacade(service, teleFacade, classBuilder);

        batchesGenerator.generate(teleFacade.batchPack());
    }

}
