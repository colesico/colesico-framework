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
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.model.teleapi.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.teleapi.TeleFacade;
import com.palantir.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import javax.lang.model.element.Modifier;

import static colesico.framework.teleapi.TeleFacade.TARGET_PROV_FIELD;

/**
 * Generate tele-facade class
 */
public class TeleFacadeGenerator {

    public static final String TARGET_INSTANCE_VAR = "target";

    protected final Logger logger = LoggerFactory.getLogger(TeleFacadeGenerator.class);

    protected final ServiceProcessorContext context;
    protected final VarNameSequence varNames = new VarNameSequence();

    protected final TeleBatchesGenerator batchesGenerator;

    public TeleFacadeGenerator(ServiceProcessorContext context) {
        this.context = context;
        this.batchesGenerator = new TeleBatchesGenerator(context.processingEnv());
    }

    protected void generateConstructor(TeleServiceElement teleService, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleService.parentService().originClass().originType())),
                TARGET_PROV_FIELD,
                Modifier.FINAL);

        mb.addStatement("super($N)", TARGET_PROV_FIELD);
        classBuilder.addMethod(mb.build());
    }


    protected void generateTeleMethods(TeleServiceElement teleService, TypeSpec.Builder classBuilder) {
        for (TeleCommandElement teleCommand : teleService.teleCommands()) {

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(teleCommand.commandMethodName());
            methodBuilder.addJavadoc("Tele-command method for target method '$N'", teleCommand.serviceMethod().name());
            methodBuilder.addModifiers(Modifier.PUBLIC);
            methodBuilder.returns(TypeName.VOID);

            //======================== Get service instance and call service method

            CodeBlock.Builder cb = CodeBlock.builder();
            // Get service instance:  TargetType target = targetProvider.get();
            ServiceElement service = teleCommand.parentTeleService().parentService();
            TypeName serviceTypeName = TypeName.get(service.originClass().originType());
            cb.addStatement("final $T $N = $N.get()", serviceTypeName,
                    TARGET_INSTANCE_VAR,
                    TeleFacade.TARGET_PROV_FIELD);

            // Collect params
            ArrayCodegen serviceMethodArgs = new ArrayCodegen();
            for (TeleParameterElement param : teleCommand.parameters()) {
                serviceMethodArgs.add("null");
            }

            // Call service method
            // target.method(null,null..);
            cb.add(TARGET_INSTANCE_VAR + "." + teleCommand.serviceMethod().name()
                    + "(" + serviceMethodArgs.toFormat() + ");\n", serviceMethodArgs.toValues());

            methodBuilder.addCode(cb.build());
            classBuilder.addMethod(methodBuilder.build());

        }
    }

    protected void generateCommandsMethod(TeleServiceElement teleService, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleFacade.COMMANDS_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(teleService.commandsClass()));
        mb.addCode(teleService.commandsMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createTeleFacadeClassFile(ServiceElement service, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = service.originClass().packageName();
        CodegenUtils.createJavaFile(context.processingEnv(), typeSpec, packageName, service.originClass().unwrap());
    }

    public void generate(ServiceElement service) {
        TeleServiceElement teleService = service.teleService();
        if (teleService == null) {
            return;
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(teleService.facadeClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Service: " + service.originClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(TeleFacade.class),
                TypeName.get(service.originClass().originType()),
                ClassName.get(teleService.commandsClass())));

        generateConstructor(teleService, classBuilder);
        generateTeleMethods(teleService, classBuilder);
        generateCommandsMethod(teleService, classBuilder);

        createTeleFacadeClassFile(service, classBuilder);

        batchesGenerator.generate(teleService.batchPack());
    }

}
