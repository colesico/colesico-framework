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
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ProcessorContext;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.TeleDriver;
import colesico.framework.teleapi.TeleFacade;
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
import static colesico.framework.teleapi.TeleFacade.TELEDRIVER_FIELD;

/**
 * @author Vladlen Larionov
 */
public class TeleFacadesGenerator {

    public static final String PARAM_SUFFIX = "Arg";
    protected final Logger logger = LoggerFactory.getLogger(TeleFacadesGenerator.class);

    protected final ProcessorContext context;
    protected final VarNameSequence varNames = new VarNameSequence();

    public TeleFacadesGenerator(ProcessorContext context) {
        this.context = context;
    }

    protected void generateCounstructor(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
            ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleFacade.getParentService().getOriginClass().asType())),
            TARGET_PROV_FIELD,
            Modifier.FINAL);

        mb.addParameter(ClassName.get(teleFacade.getTeleDriverClass()), TELEDRIVER_FIELD, Modifier.FINAL);
        mb.addStatement("super($N,$N)", TARGET_PROV_FIELD, TELEDRIVER_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected CodeBlock generateVarValue(TeleVarElement var, CodeBlock.Builder binderBuilder) {

        // detect param type considering generics
        TypeMirror paramType = var.getOriginVariable().asClassType().unwrap();

        if (var instanceof TeleParamElement) {
            CodeBlock ctx = ((TeleParamElement) var).getReadingContext();
            // Generates code like this: dataPot.read(ParamType.class, new Context(...));
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("$N.$N(", TeleDriver.Binder.DATAPORT_PARAM, DataPort.READ_FOR_CLASS_METHOD);
            // ParamType.class or new TypeWrapper<ParamType<T>>...
            CodegenUtils.generateTypePick(paramType, cb);cb.add(",");
            cb.add(ctx);
            cb.add(")");
            return cb.build();
        }

        // == Generate composition

        final String valueVar = varNames.getNextTempVariable(var.getOriginVariable().getName());
        binderBuilder.add("\n// Init composition\n");
        binderBuilder.addStatement("$T $N = new $T()",
            TypeName.get(paramType),
            valueVar, TypeName.get(var.getOriginVariable().asType()));

        // Generate composition fields
        for (TeleVarElement subvar : ((TeleCompElement) var).getVariables()) {
            CodeBlock value = generateVarValue(subvar, binderBuilder);
            String setterName = "set" + StrUtils.firstCharToUpperCase(subvar.getOriginVariable().getName());
            binderBuilder.add("$N.$N(", valueVar, setterName);
            binderBuilder.add(value);
            binderBuilder.add(");\n");
        }
        binderBuilder.add("\n");
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add(valueVar);
        return cb.build();

    }

    protected CodeBlock generateBinderBody(TeleMethodElement teleMethod) {
        MethodElement originMethod = teleMethod.getProxyMethod().getOriginMethod();
        CodeBlock.Builder binderBuilder = CodeBlock.builder();

        // ============= Generate params model retrieving
        ArrayCodegen serviceMethodArgs = new ArrayCodegen();
        for (TeleVarElement param : teleMethod.getParameters()) {
            CodeBlock value = generateVarValue(param, binderBuilder);
            String paramName = param.getOriginVariable().getName() + PARAM_SUFFIX;
            serviceMethodArgs.add("$N", paramName);
            binderBuilder.add("\n// Assign telemethod parameter value \n");
            binderBuilder.add("$T $N = ", TypeName.get(param.getOriginVariable().asType()), paramName);
            binderBuilder.add(value);
            binderBuilder.add(";\n");
        }

        TypeMirror returnType = originMethod.getReturnType();
        boolean voidResult = returnType instanceof NoType;

        //======================== Get service instance and call service method

        // Create writer variable
        //   MyResult result =
        CodeBlock.Builder callMethodCb = CodeBlock.builder();
        callMethodCb.add("\n // Invoke target service method\n");
        if (!voidResult) {
            callMethodCb.add("$T $N = ", TypeName.get(returnType), TeleDriver.RESULT_PARAM);
        }

        // Call service method
        //   target.myMethod(...);
        callMethodCb.add(TeleDriver.Binder.TARGET_PARAM + "." + teleMethod.getProxyMethod().getName() + "(" + serviceMethodArgs.toFormat() + ");\n", serviceMethodArgs.toValues());
        binderBuilder.add(callMethodCb.build());

        // Send result to client via data port
        //    dataPort.write(MyResp.class,result,new Ctx());
        // or  for generics: dataPort.write(new TypeWrapper<MyResp>(){}.unwrap(),result,new Ctx());
        if (!voidResult) {
            binderBuilder.add("$N.$N(", TeleDriver.Binder.DATAPORT_PARAM, DataPort.WRITE_FOR_TYPE_METHOD);
            CodegenUtils.generateTypePick(returnType, binderBuilder);
            binderBuilder.add(", ");
            binderBuilder.add("$N, ", TeleDriver.RESULT_PARAM);
            CodeBlock writeCtx = teleMethod.getWritingContext();
            binderBuilder.add(writeCtx);
            binderBuilder.add(");\n");
        }

        return binderBuilder.build();
    }

    protected void generateTeleMethods(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        ServiceElement service = teleFacade.getParentService();
        for (TeleMethodElement teleMethod : teleFacade.getTeleMethods()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(teleMethod.getName());
            methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            methodBuilder.returns(TypeName.VOID);

            // Subroutine definition
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("final $T $N=($N,$N)->{\n",
                ParameterizedTypeName.get(
                    ClassName.get(TeleDriver.Binder.class),
                    TypeName.get(service.getOriginClass().asType()),
                    ClassName.get(teleFacade.getDataPortClass())),
                TeleDriver.BINDER_PARAM,
                TeleDriver.Binder.TARGET_PARAM,
                TeleDriver.Binder.DATAPORT_PARAM);
            cb.indent();
            cb.add(generateBinderBody(teleMethod));
            cb.unindent();
            cb.add("};\n");

            // Get service instance:  Service service=serviceProv.get();
            TypeName serviceTypeName = TypeName.get(teleFacade.getParentService().getOriginClass().asType());
            cb.addStatement("$T $N=$N.get()", serviceTypeName, TeleDriver.TARGET_PARAM, TeleFacade.TARGET_PROV_FIELD);

            // Call teleDriver
            cb.add("$N.$N($N,$N,", TeleFacade.TELEDRIVER_FIELD, TeleDriver.INVOKE_METHOD,
                TeleDriver.TARGET_PARAM,
                TeleDriver.BINDER_PARAM);
            CodeBlock invCtx = teleMethod.getInvokingContext();
            cb.add(invCtx);
            cb.add(");\n");
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

    public void generateTeleFacades(ServiceElement service) {

        this.varNames.reset();

        for (TeleFacadeElement teleFacade : service.getTeleFacades()) {
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(teleFacade.getFacadeClassSimpleName());
            classBuilder.addModifiers(Modifier.PUBLIC);
            classBuilder.addModifiers(Modifier.FINAL);

            AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Service: " + service.getOriginClass().unwrap().getQualifiedName().toString());
            classBuilder.addAnnotation(genstamp);

            classBuilder.addAnnotation(ClassName.get(Singleton.class));


            classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(TeleFacade.class),
                TypeName.get(service.getOriginClass().asType()),
                ClassName.get(teleFacade.getTeleDriverClass()),
                ClassName.get(teleFacade.getLigatureClass())));

            generateCounstructor(teleFacade, classBuilder);
            generateTeleMethods(teleFacade, classBuilder);
            generateGetLigatureMethod(teleFacade, classBuilder);

            createTeleFacade(service, teleFacade, classBuilder);
        }
    }

}
