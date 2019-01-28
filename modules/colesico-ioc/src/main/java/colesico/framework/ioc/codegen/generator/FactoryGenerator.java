/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.ioc.codegen.generator;

import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.codegen.model.CustomFactoryElement;
import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.InjectableElement;
import colesico.framework.ioc.codegen.model.ScopeElement;
import colesico.framework.assist.LazySingleton;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.ioc.ioclet.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static colesico.framework.ioc.codegen.generator.IocletGenerator.PRODUCER_FIELD;

/**
 * @author Vladlen Larionov
 */
public class FactoryGenerator {

    public static final String IOC_FIELD = "ioc";

    protected final KeyGenerator keyGenerator = new KeyGenerator();

    public TypeSpec generateFactory(FactoryElement factoryElement) {
        switch (factoryElement.getScope().getKind()) {
            case SINGLETON:
                return generateSingletonFactory(factoryElement);
            case CUSTOM:
                return generateScopedFactory(factoryElement);
            case UNSCOPED:
                return generateUnscopedFactory(factoryElement);
            default:
                throw CodegenException.of().message("Unsupported scope kind: " + factoryElement.getScope().getKind()).build();
        }

    }

    protected void generateSetupMethod(TypeSpec.Builder factoryBuilder, FactoryElement factoryElement) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(SingletonFactory.SETUP_METHOD);
        methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        methodBuilder.addAnnotation(Override.class);
        methodBuilder.addParameter(TypeName.get(AdvancedIoc.class), Factory.IOC_PARAM, Modifier.FINAL);
        methodBuilder.returns(TypeName.VOID);

        // Param factories fields

        boolean addIocField = false;
        for (InjectableElement parameter : factoryElement.getParameters()) {

            // Dynamic binding
            if (parameter.getLinkagePhase() == InjectableElement.LinkagePhase.PRODUCTION) {
                addIocField = true;
                continue;
            }

            String factoryMethodName = getFactoryMethodName(parameter);
            if (factoryMethodName == null) {
                // Ignore message injection
                continue;
            }

            String factoryFieldName = getFactoryVarName(parameter);
            generateField(factoryBuilder, getFactoryTypeName(parameter), factoryFieldName);
            methodBuilder.addStatement("this.$N=$N.$N($L)",
                    factoryFieldName,
                    Factory.IOC_PARAM,
                    factoryMethodName,
                    keyGenerator.forInjection(parameter));
        }

        // Scope prov field
        if (factoryElement.getScope().getKind() == ScopeElement.ScopeKind.CUSTOM) {
            methodBuilder.addStatement("this.$N=$N.$N($L,null)",
                    ScopedFactory.SCOPE_FACTORY_FIELD,
                    Factory.IOC_PARAM,
                    AdvancedIoc.FACTORY_METHOD,
                    keyGenerator.forScope(factoryElement.getScope().getScopeClass().toString()));
        }

        // Dynamic binding
        if (addIocField) {
            generateIocField(factoryBuilder);
            methodBuilder.addStatement("this.$N=$N", IOC_FIELD, Factory.IOC_PARAM);
        }

        factoryBuilder.addMethod(methodBuilder.build());
    }


    protected CodeBlock generateInstanceCreation(MethodSpec.Builder methodBuilder, FactoryElement factoryElement) {
        CodeBlock.Builder cb = CodeBlock.builder();
        if (factoryElement instanceof CustomFactoryElement) {
            CustomFactoryElement cse = (CustomFactoryElement) factoryElement;
            cb.add("$N.$N().$N(", PRODUCER_FIELD, LazySingleton.GET_METHOD,
                    cse.getProducerMethod().getSimpleName());
        } else {
            cb.add("new $T(", TypeName.get(factoryElement.getSuppliedType()));
        }

        ArrayCodegen arrayCodegen = new ArrayCodegen();

        for (InjectableElement param : factoryElement.getParameters()) {

            String messageSource;
            switch (param.getMessageKind()) {
                case INJECTION_POINT:
                    messageSource = param.getOriginParameter().getSimpleName() + "Msg";
                    methodBuilder.addStatement("final $T $N = new $T($T.class)",
                            ClassName.get(InjectionPoint.class),
                            messageSource,
                            ClassName.get(InjectionPoint.class),
                            TypeName.get(param.getParentFactory().getSuppliedType()));

                    break;
                case OUTER_MESSAGE:
                    messageSource = Factory.MESSAGE_PARAM;
                    break;
                default:
                    messageSource = null;
            }

            String factorySource;
            // Is static binding?
            if (param.getLinkagePhase() == InjectableElement.LinkagePhase.ACTIVATION) {
                // Use factory from field
                factorySource = "this." + getFactoryVarName(param);
            } else {
                // Use factory from local variable
                String factoryMethodName = getFactoryMethodName(param);
                if (factoryMethodName != null) {
                    factorySource = param.getOriginParameter().getSimpleName() + "Var";
                    methodBuilder.addStatement("final $T $N=$N.$N($L)",
                            getFactoryTypeName(param),
                            factorySource,
                            Factory.IOC_PARAM,
                            factoryMethodName,
                            keyGenerator.forInjection(param));
                } else {
                    factorySource = null;
                }
            }

            switch (param.getInjectionKind()) {
                case MESSAGE:
                    arrayCodegen.add("($T)$N", TypeName.get(param.getInjectedType()), messageSource);
                    break;
                case INSTANCE:
                    arrayCodegen.add("$N.$N($N)", factorySource, Factory.GET_METHOD, messageSource);
                    break;
                case SUPPLIER:
                    arrayCodegen.add("new $T($N)", ClassName.get(DefaultSupplier.class), factorySource);
                    break;
                case PROVIDER:
                    arrayCodegen.add("new $T($N,$N)", ClassName.get(DefaultProvider.class), factorySource, messageSource);
                    break;
                case POLYSUPPLIER:
                    arrayCodegen.add("new $T($N)", ClassName.get(DefaultPolysupplier.class), factorySource);
                    break;
            }
        }
        cb.add(arrayCodegen.toFormat() + ")", arrayCodegen.toValues());
        return cb.build();
    }

    protected void generateGetMethod(TypeSpec.Builder factoryBuilder, FactoryElement factoryElement) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Factory.GET_METHOD);
        methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        methodBuilder.addAnnotation(Override.class);
        methodBuilder.returns(TypeName.get(factoryElement.getSuppliedType()));
        methodBuilder.addParameter(ClassName.get(Object.class), Factory.MESSAGE_PARAM, Modifier.FINAL);
        CodeBlock instanceBlock = generateInstanceCreation(methodBuilder, factoryElement);
        methodBuilder.addStatement("return $L", instanceBlock);
        factoryBuilder.addMethod(methodBuilder.build());
    }

    protected void generateCreatorMethod(TypeSpec.Builder factoryBuilder, FactoryElement factoryElement, String methodName) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName);
        methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        methodBuilder.addAnnotation(Override.class);
        methodBuilder.returns(TypeName.get(factoryElement.getSuppliedType()));
        methodBuilder.addParameter(ClassName.get(Object.class), Factory.MESSAGE_PARAM, Modifier.FINAL);
        CodeBlock instanceBlock = generateInstanceCreation(methodBuilder, factoryElement);
        methodBuilder.addStatement("return $L", instanceBlock);
        factoryBuilder.addMethod(methodBuilder.build());
    }

    protected TypeSpec generateSingletonFactory(FactoryElement factoryElement) {
        TypeSpec.Builder factoryBuilder = TypeSpec.anonymousClassBuilder("");
        factoryBuilder.superclass(ParameterizedTypeName.get(ClassName.get(SingletonFactory.class), TypeName.get(factoryElement.getSuppliedType())));
        generateSetupMethod(factoryBuilder, factoryElement);
        generateCreatorMethod(factoryBuilder, factoryElement, SingletonFactory.CREATE_METHOD);
        return factoryBuilder.build();
    }

    protected TypeSpec generateScopedFactory(FactoryElement factoryElement) {
        TypeSpec.Builder classBuilder = TypeSpec.anonymousClassBuilder("$L", keyGenerator.forFactory(factoryElement));
        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(ScopedFactory.class),
                TypeName.get(factoryElement.getSuppliedType()),
                TypeName.get(factoryElement.getScope().getScopeClass())));
        generateSetupMethod(classBuilder, factoryElement);
        generateCreatorMethod(classBuilder, factoryElement, ScopedFactory.FABRICATE_METHOD);
        return classBuilder.build();
    }

    protected TypeSpec generateUnscopedFactory(FactoryElement factoryElement) {
        TypeSpec.Builder factoryBuilder = TypeSpec.anonymousClassBuilder("");
        factoryBuilder.superclass(ParameterizedTypeName.get(ClassName.get(Factory.class),
                TypeName.get(factoryElement.getSuppliedType())));
        generateSetupMethod(factoryBuilder, factoryElement);
        generateGetMethod(factoryBuilder, factoryElement);
        return factoryBuilder.build();
    }

    public TypeName getFactoryTypeName(InjectableElement injectionElement) {
        String className = injectionElement.getInjectedType().asElement().toString();
        return ParameterizedTypeName.get(ClassName.get(Factory.class), ClassName.bestGuess(className));
    }

    protected String getFactoryVarName(InjectableElement injectionElement) {
        return injectionElement.getOriginParameter().getSimpleName() + "Fac";
    }

    protected void generateField(TypeSpec.Builder factoryBuilder, TypeName typeName, String fieldName) {
        FieldSpec.Builder fb = FieldSpec.builder(typeName, fieldName);
        fb.addModifiers(Modifier.PRIVATE);
        factoryBuilder.addField(fb.build());
    }

    protected String getFactoryMethodName(InjectableElement injectableElement) {
        switch (injectableElement.getInjectionKind()) {
            case MESSAGE:
                return null;
            case POLYSUPPLIER:
                return AdvancedIoc.FACTORY_OR_NULL_METHOD;
            case SUPPLIER:
            case PROVIDER:
            case INSTANCE:
                return AdvancedIoc.FACTORY_METHOD;
            default:
                throw CodegenException.of().message("Unsupported injection kind: " + injectableElement.getInjectionKind()).element(injectableElement.getOriginParameter()).build();
        }
    }

    protected void generateIocField(TypeSpec.Builder factoryBuilder) {
        FieldSpec.Builder fb = FieldSpec.builder(ClassName.get(AdvancedIoc.class), IOC_FIELD);
        fb.addModifiers(Modifier.PRIVATE);
        factoryBuilder.addField(fb.build());
    }


}