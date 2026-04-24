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


import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.ioc.listener.PostConstruct;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.service.*;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import com.palantir.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

import static colesico.framework.service.ServiceProxy.GET_SERVICE_ORIGIN_METHOD;

/**
 * Service proxy class generator
 *
 * @author Vladlen Larionov
 */
public class ServiceProxyGenerator {
    private Logger logger = LoggerFactory.getLogger(ServiceProxyGenerator.class);

    public static final String METHOD_PARAM_PREFIX = "param";
    public static final String INTERCEPTORS_CHAIN_VAR = "interceptors";
    public static final String INV_CONTEXT_VAR = "ctx";

    protected final ServiceProcessorContext context;
    protected final TeleFacadesGenerator teleFacadesGenerator;

    public ServiceProxyGenerator(ServiceProcessorContext context) {
        this.context = context;
        teleFacadesGenerator = new TeleFacadesGenerator(context);
    }

    /**
     * Finds the constructor that be used for injection
     */
    protected MethodElement findInjectableConstructor(ServiceElement serviceElement) {

        List<MethodElement> constructors = serviceElement.originClass().constructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC) && !c.unwrap().getModifiers().contains(Modifier.FINAL)
        );
        MethodElement constructor = null;
        MethodElement firstConstructor = null;
        for (MethodElement method : constructors) {
            if (firstConstructor == null) {
                firstConstructor = method;
            }
            AnnotationAssist<Inject> constructMarker = method.annotation(Inject.class);
            if (constructMarker == null) {
                continue;
            }
            constructor = method;
            break;
        }
        if (constructor == null) {
            constructor = firstConstructor;
        }
        return constructor;
    }

    /**
     * Find appropriate similar parameter to avoid duplicate injections
     */
    protected ParameterElement findSimilarParam(ServiceFieldElement fieldElement, List<ParameterElement> constructorParams) {

        ParameterElement similarParam = null;
        String fieldNamedVal = fieldElement.named() != null ? fieldElement.named() : "";
        TypeName fieldClassedVal = fieldElement.classed() != null ? fieldElement.classed() : ClassName.get(Object.class);

        for (ParameterElement paramElm : constructorParams) {
            TypeName paramType = TypeName.get(paramElm.originType());
            AnnotationAssist<Named> namedAnn = paramElm.annotation(Named.class);
            AnnotationAssist<Classed> classedAnn = paramElm.annotation(Classed.class);

            if (paramType.equals(fieldElement.injectAs())) {

                String annNamedVal = namedAnn != null ? namedAnn.unwrap().value() : "";
                TypeName annClassedVal = classedAnn != null ? TypeName.get(classedAnn.valueTypeMirror(Classed::value))
                        : ClassName.get(Object.class);

                if (annNamedVal.equals(fieldNamedVal) && annClassedVal.equals(fieldClassedVal)) {
                    similarParam = paramElm;
                    break;
                }
            }
        }
        return similarParam;
    }

    protected void generateConstructor(ServiceElement serviceElement, TypeSpec.Builder serviceBuilder) {
        // Find effective constructor
        MethodElement constructor = findInjectableConstructor(serviceElement);

        MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                constructor, null, METHOD_PARAM_PREFIX, false
        );

        constructorBuilder.addAnnotation(Inject.class);
        CodeBlock sucall = CodegenUtils.generateSuperMethodCall(constructor, null, METHOD_PARAM_PREFIX);
        constructorBuilder.addCode(sucall);

        // Generate extra fields initialization
        List<ParameterElement> constructorParams = constructor.parameters();
        for (ServiceFieldElement fieldElm : serviceElement.customFields()) {
            if (fieldElm.injectAs() == null) {
                continue;
            }

            logger.debug("Generate field initialization:" + fieldElm);

            // Find appropriate existing parameter for field initialization
            ParameterElement similarParam = findSimilarParam(fieldElm, constructorParams);

            if (similarParam == null) {
                ParameterSpec.Builder pb = ParameterSpec.builder(fieldElm.injectAs(), fieldElm.spec().name());
                if (StringUtils.isNotEmpty(fieldElm.named())) {
                    AnnotationSpec.Builder named = AnnotationSpec.builder(Named.class);
                    named.addMember("value", "$S", fieldElm.named());
                    pb.addAnnotation(named.build());
                } else if (fieldElm.classed() != null) {
                    AnnotationSpec.Builder named = AnnotationSpec.builder(Classed.class);
                    named.addMember("value", "$T", fieldElm.classed());
                    pb.addAnnotation(named.build());
                }

                constructorBuilder.addParameter(pb.build());
                constructorBuilder.addStatement("this.$N = $N", fieldElm.spec().name(), fieldElm.spec().name());
            } else {
                String paramName = StrUtils.addPrefix(METHOD_PARAM_PREFIX, similarParam.name());
                constructorBuilder.addStatement("this.$N = $N", fieldElm.spec().name(), paramName);
            }
        }

        // Generate constructor body extra code
        for (CodeBlock cb : serviceElement.constructorCustomCode()) {
            constructorBuilder.addCode(cb);
        }

        serviceBuilder.addMethod(constructorBuilder.build());
    }

    protected void generateGetSuperClassMethod(ServiceElement serviceElm) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(GET_SERVICE_ORIGIN_METHOD);
        mb.addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)))
                .addStatement("return $T.class", TypeName.get(serviceElm.originClass().originType()));

        serviceElm.addCustomMethod(new CustomMethodElement(mb.build()));
    }

    protected InterceptionElement generateSuperMethodInterception(MethodElement methodElement) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        codeBlock.add("$N->{", Interceptor.INVOCATION_CONTEXT_PARAM);
        codeBlock.add("\n");
        codeBlock.indent();

        // Process method parameters
        List<ParameterElement> methodParams = methodElement.parameters();

        if (!methodParams.isEmpty()) {
            codeBlock.addStatement("$T[] p = " + Interceptor.INVOCATION_CONTEXT_PARAM + "." + InvocationContext.PARAMETERS_METHOD + "()", ClassName.get(Object.class));
        }

        List paramItems = new ArrayList();
        List<String> paramFormats = new ArrayList<>();

        int i = 0;
        for (ParameterElement paramElm : methodParams) {
            String paramName = "p[" + i + "]";
            paramItems.add(TypeName.get(paramElm.originType()));
            paramItems.add(paramName);
            paramFormats.add("($T)$N");
            i++;
        }

        String methodName = methodElement.name();
        TypeMirror retType = methodElement.returnType();
        paramItems.add(0, methodName);
        Object[] statementArgs = paramItems.toArray(new Object[paramItems.size()]);
        if (retType instanceof NoType) {
            codeBlock.addStatement("super.$N(" + StringUtils.join(paramFormats, ",") + ")", statementArgs);
            codeBlock.addStatement("return null");
        } else {
            codeBlock.addStatement("return super.$N(" + StringUtils.join(paramFormats, ",") + ")", statementArgs);
        }
        codeBlock.unindent();
        codeBlock.add("}");
        return new InterceptionElement(codeBlock.build());
    }

    protected void generateInvocationContextExec(MethodElement methodElement, MethodSpec.Builder serviceMethodBuilder) {
        List<ParameterElement> methodParams = methodElement.parameters();
        List<String> paramNames = new ArrayList<>();
        for (ParameterElement paramElm : methodParams) {
            paramNames.add(StrUtils.addPrefix(METHOD_PARAM_PREFIX, paramElm.name()));
        }
        String paramsArrayLiteral = "{" + String.join(",", paramNames) + "}";

        serviceMethodBuilder.addStatement("final $T " + INV_CONTEXT_VAR + "= new $T(this,$S,new $T[]$L," + INTERCEPTORS_CHAIN_VAR + ")",
                ClassName.get(InvocationContext.class),
                ClassName.get(InvocationContext.class),
                methodElement.name(),
                ArrayTypeName.get(Object.class), paramsArrayLiteral);
        TypeMirror returnType = methodElement.returnType();
        boolean voidResult = returnType instanceof NoType;

        if (voidResult) {
            serviceMethodBuilder.addStatement(INV_CONTEXT_VAR + "." + InvocationContext.PROCEED_METHOD + "()");
        } else {
            serviceMethodBuilder.addStatement("return ($T)" + INV_CONTEXT_VAR + "." + InvocationContext.PROCEED_METHOD + "()",
                    TypeName.get(methodElement.returnType()));
        }
    }

    protected void generateServiceMethods(ServiceElement serviceElement, TypeSpec.Builder serviceBuilder) {
        for (ServiceMethodElement methodElement : serviceElement.serviceMethods()) {
            logger.debug("Generate proxy method:{}; isPlain={}; isLocal={}; isPCListener={}", methodElement.name(), methodElement.isPlain(), methodElement.isLocal(), methodElement.isPostConstructListener());
            if (methodElement.isPlain()) {
                logger.debug("Skip plain method: {}", methodElement.name());
                continue;
            }

            MethodSpec.Builder methodBuilder = CodegenUtils.createProxyMethodBuilder(
                    methodElement.originMethod(), null, METHOD_PARAM_PREFIX, true
            );

            if (methodElement.isPostConstructListener()) {
                methodBuilder.addAnnotation(PostConstruct.class);
            }

            TypeName returnTypeName;
            if (methodElement.originMethod().isVoidReturnType()) {
                returnTypeName = TypeName.get(Object.class);
            } else {
                returnTypeName = TypeName.get(methodElement.originMethod().returnType());
            }

            methodBuilder.addStatement("final $T " + INTERCEPTORS_CHAIN_VAR + " = new $T<>()",
                    ParameterizedTypeName.get(ClassName.get(InterceptorsChain.class),
                            TypeName.get(serviceElement.originClass().originType()),
                            returnTypeName),
                    ClassName.get(InterceptorsChain.class));


            // Adds interceptors code for each interception  phase
            for (String intrcPhase : context.interceptionPhases().phaseOrder()) {
                for (InterceptionElement intrcBinding : methodElement.phaseInterceptions(intrcPhase)) {
                    generateInterceptorBindings(intrcBinding, methodBuilder);
                }
            }

            // Super method call
            if (methodElement.interception() != null) {
                generateInterceptorBindings(methodElement.interception(), methodBuilder);
            } else {
                InterceptionElement ihe = generateSuperMethodInterception(methodElement.originMethod());
                generateInterceptorBindings(ihe, methodBuilder);
            }

            // Add invContext.proceed...
            generateInvocationContextExec(methodElement.originMethod(), methodBuilder);
            serviceBuilder.addMethod(methodBuilder.build());
        }
    }

    private void generateInterceptorBindings(InterceptionElement interceptionElement, MethodSpec.Builder serviceMethodBuilder) {
        CodeBlock.Builder interceptorBindings = CodeBlock.builder();
        interceptorBindings.add(INTERCEPTORS_CHAIN_VAR + ".add(");
        interceptorBindings.add(interceptionElement.interceptorCode());
        interceptorBindings.add(",");

        if (interceptionElement.parametersCode() != null) {
            interceptorBindings.add(interceptionElement.parametersCode());
        } else {
            interceptorBindings.add("null");
        }
        interceptorBindings.add(");\n");
        serviceMethodBuilder.addCode(interceptorBindings.build());
    }

    public void generate(ServiceElement serviceElement) {

        TypeSpec.Builder serviceBuilder = TypeSpec.classBuilder(serviceElement.proxyClassSimpleName()).superclass(TypeName.get(serviceElement.originClass().originType()));
        serviceBuilder.addSuperinterface(ClassName.get(ServiceProxy.class));
        serviceBuilder.addModifiers(Modifier.PUBLIC);
        serviceBuilder.addModifiers(Modifier.FINAL);

        // Proxy class annotations
        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Origin: " + serviceElement.originClass().unwrap().getQualifiedName().toString());
        serviceBuilder.addAnnotation(genstamp);

        AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.get(Unscoped.class));
        serviceBuilder.addAnnotation(scopeAnnBuilder.build());

        AnnotationSpec.Builder originAnnBuilder = AnnotationSpec.builder(ClassName.get(ServiceOrigin.class));
        originAnnBuilder.addMember("value", "$T.class", TypeName.get(serviceElement.originClass().originType()));
        serviceBuilder.addAnnotation(originAnnBuilder.build());

        // Custom interfaces
        for (TypeName extraInterface : serviceElement.customInterfaces()) {
            serviceBuilder.addSuperinterface(extraInterface);
        }

        // Generate class fields
        for (ServiceFieldElement fh : serviceElement.customFields()) {
            serviceBuilder.addField(fh.spec());
        }

        // Custom constructor
        generateConstructor(serviceElement, serviceBuilder);

        generateGetSuperClassMethod(serviceElement);

        // Proxy methods
        generateServiceMethods(serviceElement, serviceBuilder);

        // Custom methods
        for (CustomMethodElement customMethod : serviceElement.customMethods()) {
            serviceBuilder.addMethod(customMethod.spec());
        }

        // Build class file
        ClassElement originClass = serviceElement.originClass();
        final TypeSpec typeSpec = serviceBuilder.build();
        String packageName = originClass.packageName();
        CodegenUtils.createJavaFile(context.processingEnv(), typeSpec, packageName, originClass.unwrap());

        teleFacadesGenerator.generate(serviceElement);

        context.modulatorKit().notifyServiceGenerated(serviceElement);
    }
}
