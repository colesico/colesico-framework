/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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


import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.teleapi.*;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;

import static colesico.framework.teleapi.TeleFacade.TARGET_PROV_FIELD;
import static colesico.framework.teleapi.TeleFacade.TELE_DRIVER_FIELD;

/**
 * Generate tele-facade class
 */
public class TeleFacadesGenerator {

    public static final String PARAM_SUFFIX = "Param";
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
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleFacade.getParentService().getOriginClass().getOriginType())),
                TARGET_PROV_FIELD,
                Modifier.FINAL);

        mb.addParameter(ClassName.get(teleFacade.getTeleDriverClass()), TELE_DRIVER_FIELD, Modifier.FINAL);
        mb.addStatement("super($N, $N)", TARGET_PROV_FIELD, TELE_DRIVER_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected CodeBlock generateBatches(TeleMethodElement teleMethod) {
        CodeBlock.Builder cb = CodeBlock.builder();
        for (TeleBatchElement batch : teleMethod.getBatches().values()) {
            if (batch.getReadingContext() == null || batch.getReadingContext().getCreationCode() == null) {
                throw CodegenException.of()
                        .message("Batch reading context code not defined")
                        .element(teleMethod.getServiceMethod().getOriginMethod())
                        .build();
            }
            // BatchType batch = dataPort.read()
            cb.add("\n// Read batch \n");
            cb.add("$T $N = $N.$N(",
                    ClassName.bestGuess(batch.getBatchClassName()),
                    batch.getBatchVarName(),
                    MethodInvoker.DATA_PORT_PARAM, DataPort.READ_METHOD);
            cb.add(batch.getReadingContext().getCreationCode());
            cb.add(");\n");
        }
        return cb.build();
    }

    protected CodeBlock generateParamRetrieving(TeleEntryElement entry, CodeBlock.Builder invokerBuilder) {

        // ==== For simple param

        if (entry instanceof TeleParameterElement) {
            CodeBlock ctx = ((TeleParameterElement) entry).getReadingContext().getCreationCode();
            // dataPot.read(new Context(...));
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N(", MethodInvoker.DATA_PORT_PARAM, DataPort.READ_METHOD);
            cb.add(ctx);
            cb.add(")");
            return cb.build();
        }

        // ==== For batch filed param

        if (entry instanceof TeleBatchFieldElement) {
            TeleBatchFieldElement batchParam = (TeleBatchFieldElement) entry;
            // batch.getFiled();
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N()", batchParam.getParentBatch().getBatchVarName(), batchParam.getterName());
            return cb.build();
        }

        // ==== For compound

        final String compVar = varNames.getNextTempVariable(entry.getOriginElement().getName());
        invokerBuilder.add("\n// Init compound\n");
        TypeMirror paramType = entry.getOriginElement().asClassType().unwrap();
        invokerBuilder.addStatement("$T $N = new $T()",
                TypeName.get(paramType),
                compVar, TypeName.get(entry.getOriginElement().getOriginType()));

        // Generate compound fields

        for (TeleEntryElement field : ((TeleCompoundElement) entry).getFields()) {
            CodeBlock value = generateParamRetrieving(field, invokerBuilder);
            String setterName = "set" + StrUtils.firstCharToUpperCase(field.getOriginElement().getName());
            invokerBuilder.add("$N.$N(", compVar, setterName);
            invokerBuilder.add(value);
            invokerBuilder.add(");\n");
        }
        invokerBuilder.add("\n");
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add(compVar);
        return cb.build();
    }

    protected CodeBlock generateInvoker(TeleMethodElement teleMethod) {
        MethodElement originMethod = teleMethod.getServiceMethod().getOriginMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        // Generate invoker variable
        cb.add("\n// Create invoker closure \n");
        ParameterizedTypeName invokerType = ParameterizedTypeName.get(
                ClassName.get(MethodInvoker.class),
                TypeName.get(teleMethod.getParentTeleFacade().getParentService().getOriginClass().getOriginType()),
                ClassName.get(teleMethod.getParentTeleFacade().getDataPortClass())
        );
        cb.add("$T $N = ($N, $N) -> {\n", invokerType, TeleDriver.INVOKER_PARAM, MethodInvoker.TARGET_PARAM, MethodInvoker.DATA_PORT_PARAM);
        cb.indent();

        // ============= Generate Batches retrieving
        cb.add(generateBatches(teleMethod));

        // ============= Generate params retrieving
        ArrayCodegen serviceMethodArgs = new ArrayCodegen();
        for (TeleEntryElement entry : teleMethod.getParameters()) {
            CodeBlock value = generateParamRetrieving(entry, cb);
            String paramName = entry.getOriginElement().getName() + PARAM_SUFFIX;
            serviceMethodArgs.add("$N", paramName);
            cb.add("\n// Assign tele-method parameter value from remote client or batch\n");
            cb.add("$T $N = ", TypeName.get(entry.getOriginElement().getOriginType()), paramName);
            cb.add(value);
            cb.add(";\n");
        }

        TypeMirror returnType = originMethod.getReturnType();
        boolean voidResult = returnType instanceof NoType;

        //======================== Get service instance and call service method

        // Create writer variable
        //   MyResult result =
        CodeBlock.Builder callMethodCb = CodeBlock.builder();
        callMethodCb.add("\n// Invoke target service method\n");
        if (!voidResult) {
            callMethodCb.add("$T $N = ", TypeName.get(returnType), TeleDriver.RESULT_PARAM);
        }

        // Call service method
        //   target.myMethod(...);
        callMethodCb.add(MethodInvoker.TARGET_PARAM + "." + teleMethod.getServiceMethod().getName() + "(" + serviceMethodArgs.toFormat() + ");\n", serviceMethodArgs.toValues());
        cb.add(callMethodCb.build());

        // Send result to client via data port dataPort.write(result,new Context());
        if (!voidResult) {
            cb.add("\n// Send result to remote client\n");
            cb.add("$N.$N($N,", MethodInvoker.DATA_PORT_PARAM, DataPort.WRITE_METHOD, TeleDriver.RESULT_PARAM);
            CodeBlock writeCtx = teleMethod.getWritingContext().getCreationCode();
            cb.add(writeCtx);
            cb.add(");\n");
        }

        cb.unindent();
        cb.add("};\n\n");
        return cb.build();
    }

    protected void generateTeleMethodBuilders(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        ServiceElement service = teleFacade.getParentService();
        for (TeleMethodElement teleMethod : teleFacade.getTeleMethods()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(teleMethod.getBuilderName());
            methodBuilder.addJavadoc("Returns $T instance for target method '$N'", ClassName.get(TeleMethod.class), teleMethod.getServiceMethod().getName());
            methodBuilder.addModifiers(Modifier.PRIVATE);
            methodBuilder.returns(ClassName.get(TeleMethod.class));

            // Subroutine definition
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("return () -> {\n");
            cb.indent();
            cb.add(generateInvoker(teleMethod));
            // Get service instance:  Service service=serviceProv.get();
            TypeName serviceTypeName = TypeName.get(service.getOriginClass().getOriginType());
            cb.addStatement("$T $N = $N.get()", serviceTypeName, TeleDriver.TARGET_PARAM, TeleFacade.TARGET_PROV_FIELD);

            // Call teleDriver
            cb.add("$N.$N($N, $N, ", TeleFacade.TELE_DRIVER_FIELD, TeleDriver.INVOKE_METHOD,
                    TeleDriver.TARGET_PARAM,
                    TeleDriver.INVOKER_PARAM);
            CodeBlock invCtx = teleMethod.getInvocationContext().getCreationCode();
            cb.add(invCtx);
            cb.add(");\n");
            cb.unindent();
            cb.add("};\n");

            methodBuilder.addCode(cb.build());
            classBuilder.addMethod(methodBuilder.build());
        }
    }

    protected void generateGetLigatureMethod(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleFacade.GET_LIGATURE_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(teleFacade.getLigatureClass()));
        mb.addCode(teleFacade.getLigatureMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createTeleFacade(ServiceElement service, TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = service.getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, service.getOriginClass().unwrap());
    }

    public void generate(ServiceElement service) {
        TeleFacadeElement teleFacade = service.getTeleFacade();
        if (teleFacade == null) {
            return;
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(teleFacade.getFacadeClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Service: " + service.getOriginClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(TeleFacade.class),
                TypeName.get(service.getOriginClass().getOriginType()),
                ClassName.get(teleFacade.getTeleDriverClass()),
                ClassName.get(teleFacade.getLigatureClass())));

        generateConstructor(teleFacade, classBuilder);
        generateTeleMethodBuilders(teleFacade, classBuilder);
        generateGetLigatureMethod(teleFacade, classBuilder);

        createTeleFacade(service, teleFacade, classBuilder);

        batchesGenerator.generate(teleFacade.getBatchPack());
    }

}
