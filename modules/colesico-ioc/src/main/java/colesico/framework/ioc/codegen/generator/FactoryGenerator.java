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

package colesico.framework.ioc.codegen.generator;

import colesico.framework.assist.LazySingleton;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenMode;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.ioc.exception.InstanceProducingException;
import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.codegen.model.CustomFactoryElement;
import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.InjectableElement;
import colesico.framework.ioc.codegen.model.ScopeElement;
import colesico.framework.ioc.ioclet.*;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;

import static colesico.framework.ioc.codegen.generator.IocletGenerator.PRODUCER_FIELD;

/**
 * @author Vladlen Larionov
 */
public class FactoryGenerator extends FrameworkAbstractGenerator {

    public static final String IOC_FIELD = "ioc";
    public static final String POST_PRODUCE_FACTORY_FIELD = "postProduceFac";
    public static final String INSTANCE_VAR = "instance";

    protected final KeyGenerator keyGenerator;

    public FactoryGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        keyGenerator = new KeyGenerator(processingEnv);
    }

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
        DeclaredType suppliedType = factoryElement.getSuppliedType().getErasure();
        methodBuilder.addComment("Initialize dependencies for: " + suppliedType.asElement().toString());
        methodBuilder.addCode(generateSetupMethodBody(factoryBuilder, factoryElement));
        factoryBuilder.addMethod(methodBuilder.build());
    }

    protected CodeBlock generateSetupMethodBody(TypeSpec.Builder factoryBuilder, FactoryElement factoryElement) {
        CodeBlock.Builder methodBody = CodeBlock.builder();

        // Param factories fields

        boolean addIocField = false;
        for (InjectableElement parameter : factoryElement.getParameters()) {

            String factoryMethodName = getFactoryMethodName(parameter);
            if (factoryMethodName == null) {
                // Ignore message injection
                continue;
            }

            String factoryFieldName = getFactoryVarName(parameter);
            generateField(factoryBuilder, getFactoryTypeName(parameter), factoryFieldName);
            methodBody.addStatement("this.$N = $N.$N($L)",
                factoryFieldName,
                Factory.IOC_PARAM,
                factoryMethodName,
                keyGenerator.forInjection(parameter));
        }

        // Scope prov field
        if (factoryElement.getScope().getKind() == ScopeElement.ScopeKind.CUSTOM) {
            methodBody.addStatement("this.$N = $N.$N($L)",
                ScopedFactory.SCOPE_FACTORY_FIELD,
                Factory.IOC_PARAM,
                AdvancedIoc.FACTORY_METHOD,
                keyGenerator.forScope(factoryElement.getScope().getCustomScopeClass()));
        }

        // Dynamic binding
        if (addIocField) {
            generateIocField(factoryBuilder);
            methodBody.addStatement("this.$N = $N", IOC_FIELD, Factory.IOC_PARAM);
        }

        // PostProduce listener
        if (factoryElement.isNotifyPostProduce()) {
            TypeName factoryTypeName = ParameterizedTypeName.get(ClassName.get(Factory.class), TypeName.get(factoryElement.getSuppliedType().unwrap()));
            generateField(factoryBuilder, factoryTypeName, POST_PRODUCE_FACTORY_FIELD);
            methodBody.add("// Obtain post produce listener factory:\n");
            methodBody.addStatement("this.$N = $N.$N($L)",
                POST_PRODUCE_FACTORY_FIELD, Factory.IOC_PARAM, AdvancedIoc.FACTORY_METHOD,
                keyGenerator.forPostProduceListener(factoryElement.getSuppliedType(), factoryElement.getNamed(), factoryElement.getClassed())
            );
        }

        return methodBody.build();
    }

    protected CodeBlock generateInstanceCreation(CodeBlock.Builder producingBuilder, FactoryElement factoryElement) {
        CodeBlock.Builder cb = CodeBlock.builder();
        if (factoryElement instanceof CustomFactoryElement) {
            CustomFactoryElement cfe = (CustomFactoryElement) factoryElement;
            cb.add("$N.$N().$N(", PRODUCER_FIELD, LazySingleton.GET_METHOD,
                cfe.getProducerMethod().getName());
        } else {
            cb.add("new $T(", TypeName.get(factoryElement.getSuppliedType().getErasure()));
        }

        ArrayCodegen arrayCodegen = new ArrayCodegen();

        for (InjectableElement param : factoryElement.getParameters()) {

            String messageSource;
            switch (param.getMessageKind()) {
                case INJECTION_POINT:
                    messageSource = param.getOriginParameter().getName() + "Msg";
                    producingBuilder.addStatement("final $T $N = new $T($T.class)",
                        ClassName.get(InjectionPoint.class),
                        messageSource,
                        ClassName.get(InjectionPoint.class),
                        TypeName.get(param.getParentFactory().getSuppliedType().getErasure()));

                    break;
                case OUTER_MESSAGE:
                    messageSource = Factory.MESSAGE_PARAM;
                    break;
                default:
                    messageSource = null;
            }

            String factorySource = "this." + getFactoryVarName(param);

            switch (param.getInjectionKind()) {
                case MESSAGE:
                    arrayCodegen.add("($T)$N", TypeName.get(param.getInjectedType().unwrap()), messageSource);
                    break;
                case INSTANCE:
                    if (param.isOptional()) {
                        arrayCodegen.add("$N == null ? null : $N.$N($N)", factorySource, factorySource, Factory.GET_METHOD, messageSource);
                    } else {
                        arrayCodegen.add("$N.$N($N)", factorySource, Factory.GET_METHOD, messageSource);
                    }
                    break;
                case SUPPLIER:
                    if (param.isOptional()) {
                        arrayCodegen.add("$N == null ? null : $N", factorySource, factorySource);
                    } else {
                        arrayCodegen.add("$N", factorySource);
                    }
                    break;
                case PROVIDER:
                    if (param.isOptional()) {
                        arrayCodegen.add("$N == null ? null : new $T($N,$N)", factorySource, ClassName.get(DefaultProvider.class), factorySource, messageSource);
                    } else {
                        arrayCodegen.add("new $T($N,$N)", ClassName.get(DefaultProvider.class), factorySource, messageSource);
                    }
                    break;
                case POLYSUPPLIER:
                    arrayCodegen.add("new $T($N)", ClassName.get(DefaultPolysupplier.class), factorySource);
                    break;
            }
        }
        cb.add(arrayCodegen.toFormat() + ")", arrayCodegen.toValues());
        return cb.build();
    }

    protected void generatePostProduceListenerInvocation(CodeBlock.Builder producingBuilder, FactoryElement factoryElement) {
        if (factoryElement.isNotifyPostProduce()) {
            producingBuilder.add("// Post produce listener invocation:\n");
            producingBuilder.addStatement("$N = $N.$N($N)",
                INSTANCE_VAR,
                POST_PRODUCE_FACTORY_FIELD,
                Factory.GET_METHOD,
                INSTANCE_VAR);
        }
    }

    protected void generatePostConstructListenersInvocations(CodeBlock.Builder producingBuilder, FactoryElement factoryElement) {
        if (factoryElement.isNotifyPostConstruct()) {
            producingBuilder.add("// Post construct listeners invocations:\n");
            for (MethodElement listenerMethod : factoryElement.getPostConstructListeners()) {
                producingBuilder.addStatement("$N.$N()", INSTANCE_VAR, listenerMethod.getName());
            }
        }
    }

    protected void generateProducingMethod(TypeSpec.Builder factoryBuilder, FactoryElement factoryElement, String methodName) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName);
        methodBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        methodBuilder.addAnnotation(Override.class);
        methodBuilder.returns(TypeName.get(factoryElement.getSuppliedType().getErasure()));
        methodBuilder.addParameter(ClassName.get(Object.class), Factory.MESSAGE_PARAM, Modifier.FINAL);

        CodeBlock.Builder producingBuilder = CodeBlock.builder();

        if (CodegenMode.DEFAULT == getCodegenMode()) {
            producingBuilder.add("try {\n");
            producingBuilder.indent();
            generateInstanceProducingCode(factoryElement, producingBuilder);
            producingBuilder.unindent();
            producingBuilder.add("} catch ($T ipe) {\n", ClassName.get(InstanceProducingException.class));
            producingBuilder.indent();
            producingBuilder.addStatement("throw ipe");
            producingBuilder.unindent();
            producingBuilder.add("} catch ($T t) {\n", ClassName.get(Throwable.class));
            producingBuilder.indent();
            producingBuilder.addStatement("throw new $T(t, $T.class)",
                ClassName.get(InstanceProducingException.class),
                TypeName.get(factoryElement.getSuppliedType().getErasure()));
            producingBuilder.unindent();
            producingBuilder.add("}\n");
        } else {
            generateInstanceProducingCode(factoryElement, producingBuilder);
        }

        methodBuilder.addCode(producingBuilder.build());
        factoryBuilder.addMethod(methodBuilder.build());
    }

    // Instance producing code
    private void generateInstanceProducingCode(FactoryElement factoryElement, CodeBlock.Builder producingBuilder) {
        CodeBlock instanceBlock = generateInstanceCreation(producingBuilder, factoryElement);
        producingBuilder.add("// Perform instance producing\n");
        producingBuilder.addStatement("$T $N = $L", TypeName.get(factoryElement.getSuppliedType().unwrap()), INSTANCE_VAR, instanceBlock);
        generatePostProduceListenerInvocation(producingBuilder, factoryElement);
        generatePostConstructListenersInvocations(producingBuilder, factoryElement);
        producingBuilder.addStatement("return $N", INSTANCE_VAR);
    }

    protected TypeSpec generateSingletonFactory(FactoryElement factoryElement) {
        TypeSpec.Builder factoryBuilder = TypeSpec.anonymousClassBuilder("");
        factoryBuilder.superclass(ParameterizedTypeName.get(ClassName.get(SingletonFactory.class),
            TypeName.get(factoryElement.getSuppliedType().getErasure())));
        generateSetupMethod(factoryBuilder, factoryElement);
        generateProducingMethod(factoryBuilder, factoryElement, SingletonFactory.CREATE_METHOD);
        return factoryBuilder.build();
    }

    protected TypeSpec generateScopedFactory(FactoryElement factoryElement) {
        TypeSpec.Builder classBuilder = TypeSpec.anonymousClassBuilder("$L", keyGenerator.forFactory(factoryElement));
        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(ScopedFactory.class),
            TypeName.get(factoryElement.getSuppliedType().getErasure()),
            TypeName.get(factoryElement.getScope().getCustomScopeClass().unwrap())));
        generateSetupMethod(classBuilder, factoryElement);
        generateProducingMethod(classBuilder, factoryElement, ScopedFactory.FABRICATE_METHOD);
        return classBuilder.build();
    }

    protected TypeSpec generateUnscopedFactory(FactoryElement factoryElement) {
        TypeSpec.Builder factoryBuilder = TypeSpec.anonymousClassBuilder("");
        factoryBuilder.superclass(ParameterizedTypeName.get(ClassName.get(Factory.class),
            TypeName.get(factoryElement.getSuppliedType().getErasure())));
        generateSetupMethod(factoryBuilder, factoryElement);
        generateProducingMethod(factoryBuilder, factoryElement, Factory.GET_METHOD);
        return factoryBuilder.build();
    }

    public TypeName getFactoryTypeName(InjectableElement injectionElement) {
        String typeName = injectionElement.getInjectedType().asClassElement().getName();
        return ParameterizedTypeName.get(ClassName.get(Factory.class), ClassName.bestGuess(typeName));
    }

    protected String getFactoryVarName(InjectableElement injectionElement) {
        return injectionElement.getOriginParameter().getName() + "Fac";
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
                if (injectableElement.isOptional()) {
                    return AdvancedIoc.FACTORY_OR_NULL_METHOD;
                }
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
