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
import colesico.framework.service.Interceptor;
import colesico.framework.service.InvocationContext;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleInterceptor;
import colesico.framework.teleapi.dataport.DataPort;
import com.palantir.javapoet.*;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;

import static colesico.framework.teleapi.TeleInterceptor.DATA_PORT_PROV_FIELD;

/**
 * Generate tele-facade class
 */
public class TeleInterceptorGenerator {

    public static final String PARAM_SUFFIX = "Param";
    public static final String RESULT_VAR = "result";
    public static final String DATA_PORT_VAR = "dataPort";
    public static final String PARAMS_VAR = "params";

    protected final Logger logger = LoggerFactory.getLogger(TeleInterceptorGenerator.class);

    protected final ServiceProcessorContext context;
    protected final VarNameSequence varNames = new VarNameSequence();

    protected final TeleBatchesGenerator batchesGenerator;

    public TeleInterceptorGenerator(ServiceProcessorContext context) {
        this.context = context;
        this.batchesGenerator = new TeleBatchesGenerator(context.processingEnv());
    }

    protected void generateConstructor(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);

        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), ClassName.get(DataPort.class)),
                DATA_PORT_PROV_FIELD,
                Modifier.FINAL);

        mb.addStatement("super($N)", DATA_PORT_PROV_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected CodeBlock generateBatches(TeleCommandElement teleCommand) {
        CodeBlock.Builder cb = CodeBlock.builder();
        for (TeleBatchElement batch : teleCommand.batches().values()) {
            if (batch.readSpec() == null || batch.readSpec().optionsCode() == null) {
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
                    DATA_PORT_VAR, DataPort.READ_METHOD);
            cb.add(batch.readSpec().valueTypeCode());
            var optionsCode = batch.readSpec().optionsCode();
            if (optionsCode != null) {
                cb.add(", ");
                cb.add(batch.readSpec().optionsCode());
            }
            cb.add(");\n");
        }
        return cb.build();
    }

    protected CodeBlock generateParamRetrieving(TeleParameterElement parameter, CodeBlock.Builder invokerBuilder) {

        // ==== For simple param ================

        if (parameter instanceof TeleOrdinaryParamElement p) {
            // dataPot.read(Value.class, new Context(...));
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N(", DATA_PORT_VAR, DataPort.READ_METHOD);
            cb.add(p.readSpec().valueTypeCode());
            var optionsCode = p.readSpec().optionsCode();
            if (optionsCode != null) {
                cb.add(", ");
                cb.add(optionsCode);
            }
            cb.add(")");
            return cb.build();
        }

        // ==== For batch filed param =============

        if (parameter instanceof TeleBatchParamElement) {
            TeleBatchParamElement batchParam = (TeleBatchParamElement) parameter;
            // batch.getFiled();
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N()", batchParam.parentBatch().batchVarName(), batchParam.getterName());
            return cb.build();
        }

        throw CodegenException.of().message("Unsupported tele parameter element: " + parameter).build();
    }

    protected void generateInterceptorsMethods(TeleServiceElement teleService, TypeSpec.Builder classBuilder) {
        for (TeleCommandElement teleCommand : teleService.teleCommands()) {
            MethodElement originMethod = teleCommand.serviceMethod().originMethod();
            TypeMirror returnType = originMethod.returnType();
            boolean voidResult = returnType instanceof NoType;

            TypeName returnTypeName = voidResult ? ClassName.get(Object.class) :
                    TypeName.get(teleCommand.serviceMethod().originMethod().returnType());

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(teleCommand.interceptorMethodName());
            methodBuilder.addJavadoc("Tele-interceptor method for target method '$N'", teleCommand.serviceMethod().name());
            methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            methodBuilder.returns(returnTypeName);
            methodBuilder.addParameter(
                    ParameterizedTypeName.get(ClassName.get(InvocationContext.class),
                            ClassName.get(teleService.parentService().originClass().unwrap()),
                            returnTypeName
                    ), Interceptor.INVOCATION_CONTEXT_PARAM
            );

            CodeBlock.Builder cb = CodeBlock.builder();

            // =============== Data port from provider
            cb.add("// Retrieve data port\n");
            cb.add("final $T $N = $N.get();\n",
                    ParameterizedTypeName.get(
                            ClassName.get(DataPort.class),
                            ClassName.get(teleService.readOptionsClass()),
                            ClassName.get(teleService.writeOptionsClass())
                    ),
                    DATA_PORT_VAR, DATA_PORT_PROV_FIELD
            );

            // ============= Batches retrieving from data port
            cb.add(generateBatches(teleCommand));

            // ============= Params retrieving (default from data port)
            if (!teleCommand.parameters().isEmpty()) {
                cb.add("\n// Assign  parameter values from remote client or batch\n");
                cb.add("final var $N = $N.$N();\n",
                        PARAMS_VAR,
                        Interceptor.INVOCATION_CONTEXT_PARAM,
                        InvocationContext.PARAMETERS_METHOD);
            }

            ArrayCodegen serviceMethodArgs = new ArrayCodegen();
            int paramInd = -1;
            for (TeleParameterElement param : teleCommand.parameters()) {
                paramInd++;
                if (param instanceof TeleInjectParamElement) {
                    // Skip param retrieving
                    // TODO: implement injection on service layer
                    continue;
                }
                CodeBlock value = generateParamRetrieving(param, cb);
                String paramName = param.originElement().name() + PARAM_SUFFIX;
                serviceMethodArgs.add("$N", paramName);
                cb.add("$N[$L] = ", PARAMS_VAR, paramInd);
                cb.add(value);
                cb.add(";\n");
            }

            // ==================  Invoke by context
            // final ResType result = ctx.proceed();
            cb.add("\n// Proceed next interceptors\n");
            cb.add("final $T $N = $N.$N();\n",
                    returnTypeName,
                    RESULT_VAR,
                    Interceptor.INVOCATION_CONTEXT_PARAM,
                    InvocationContext.PROCEED_METHOD);

            // ================  Send result to client via data port
            // dataPort.write(result, Result.class, new Context());

            if (!voidResult) {
                cb.add("\n// Write result to data port\n");
                cb.add("$N.$N($N, ",
                        DATA_PORT_VAR,
                        DataPort.WRITE_METHOD,
                        RESULT_VAR);

                cb.add(teleCommand.writeSpec().valueTypeCode());
                var optionsCode = teleCommand.writeSpec().optionsCode();
                if (optionsCode != null) {
                    cb.add(", ");
                    cb.add(optionsCode);
                }
                cb.add(");\n");
            }

            cb.add("return $N;", RESULT_VAR);

            methodBuilder.addCode(cb.build());
            classBuilder.addMethod(methodBuilder.build());
        }
    }

    protected void generateCommandsMethod(TeleServiceElement teleService, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleFacade.COMMANDS_REGISTRY_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(teleService.commandsClass()));
        mb.addCode(teleService.commandsMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createTeleInterceptorClassFile(ServiceElement service, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = service.originClass().packageName();
        CodegenUtils.createJavaFile(context.processingEnv(), typeSpec, packageName, service.originClass().unwrap());
    }

    public void generate(ServiceElement service) {
        TeleServiceElement teleService = service.teleService();
        if (teleService == null) {
            return;
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(teleService.interceptorClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Service: " + service.originClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        classBuilder.superclass(ParameterizedTypeName.get(
                ClassName.get(TeleInterceptor.class),
                ClassName.get(teleService.readOptionsClass()),
                ClassName.get(teleService.writeOptionsClass())
        ));

        generateConstructor(classBuilder);
        generateInterceptorsMethods(teleService, classBuilder);

        createTeleInterceptorClassFile(service, classBuilder);

        batchesGenerator.generate(teleService.batchPack());
    }

}
