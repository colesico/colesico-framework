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

package colesico.framework.service.codegen.generator;


import colesico.framework.ioc.Unscoped;
import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.*;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ProcessorContext;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.lang.model.element.*;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static colesico.framework.service.ServicePrototype.GET_SUPER_CLASS_METHOD;

/**
 * Service proxy code generator
 *
 * @author Vladlen Larionov
 */
public class ServiceProxyGenerator {
    private Logger logger = LoggerFactory.getLogger(ServiceProxyGenerator.class);

    public static final String METHOD_PARAM_PREFIX = "param";
    public static final String INTERCEPTORS_CHAIN_VARIABLE = "interceptors";
    public static final String INV_CONTEXT_VARIABLE = "ctx";

    protected final ProcessorContext context;
    protected final TeleFacadesGenerator teleFacadesGenerator;

    public ServiceProxyGenerator(ProcessorContext context) {
        this.context = context;
        teleFacadesGenerator = new TeleFacadesGenerator(context);
    }

    protected void generateProxyConstructor(ServiceElement serviceElement, TypeSpec.Builder proxyBuilder) {
        // Find effective constructor
        Elements utils = context.getProcessingEnv().getElementUtils();
        List<? extends Element> members = utils.getAllMembers(serviceElement.getOriginClass());
        List<ExecutableElement> methods = ElementFilter.constructorsIn(members);
        ExecutableElement constructor = null;
        ExecutableElement firstConstructor = null;
        for (ExecutableElement method : methods) {
            if (!method.getModifiers().contains(Modifier.PUBLIC)) {
                continue;
            }
            if (firstConstructor == null) {
                firstConstructor = method;
            }
            Construct constructorMarker = method.getAnnotation(Construct.class);
            if (constructorMarker == null) {
                continue;
            }
            constructor = method;
            break;
        }
        if (constructor == null) {
            constructor = firstConstructor;
        }

        // Generate constructor body
        // Generate super constructor call
        MethodSpec.Builder constructorBuilder = CodegenUtils.createBuilderProxyMethod(true, constructor, null, null, METHOD_PARAM_PREFIX, false,false);

        constructorBuilder.addAnnotation(Inject.class);
        CodegenUtils.addSuperMethodCall(constructorBuilder, true, constructor, null, METHOD_PARAM_PREFIX);

        // Generate extra fields initialization

        // Generate reader types map which used to avoid dublicate injections
        List<? extends VariableElement> constructorParams = constructor.getParameters();
        Map<TypeName, String> paramTypesMap = new HashMap<>();
        for (VariableElement paramElm : constructorParams) {
            String paramName = StrUtils.addPrefix(METHOD_PARAM_PREFIX, paramElm.getSimpleName().toString());
            TypeName paramType = TypeName.get(paramElm.asType());
            paramTypesMap.put(paramType, paramName);
        }

        for (FieldElement fh : serviceElement.getFields()) {
            logger.debug("Generate field initialization:" + fh);
            if (fh.isInject()) {
                if (!paramTypesMap.containsKey(fh.getInjectionClass())) {
                    constructorBuilder.addParameter(fh.getInjectionClass(), fh.getSpec().name);
                    constructorBuilder.addStatement("this.$N=$N", fh.getSpec().name, fh.getSpec().name);
                } else {
                    constructorBuilder.addStatement("this.$N=$N", fh.getSpec().name, paramTypesMap.get(fh.getInjectionClass()));
                }
            }
        }

        // Generate constructor body extra code
        for (CodeBlock cb : serviceElement.getConstructorExtraCode()) {
            constructorBuilder.addCode(cb);
        }

        //Generate PostConstruct invocations
        generatePostConstructInvocations(serviceElement, constructorBuilder);

        proxyBuilder.addMethod(constructorBuilder.build());
    }


    private void generatePostConstructInvocations(ServiceElement serviceElement, MethodSpec.Builder constructorBuilder) {
        for (ProxyMethodElement pme : serviceElement.getProxyMethods()) {
            if (pme.getOriginMethod().getAnnotation(PostConstruct.class) == null) {
                continue;
            }
            if (!pme.getOriginMethod().getParameters().isEmpty()) {
                throw CodegenException.of().message("Post construct method '"
                        + CodegenUtils.getClassName((TypeElement) pme.getOriginMethod().getEnclosingElement())
                        + "." + pme.getName() + "(...)'  should not have arguments").element(pme.getOriginMethod()).build();
            }
            constructorBuilder.addStatement("$N()", pme.getName());
        }
    }

    protected void generateGetSuperClassMethod(ServiceElement service) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(GET_SUPER_CLASS_METHOD);
        mb.addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)))
                .addStatement("return $T.class", ClassName.get(service.getOriginClass()));

        service.addCustomMethod(new CustomMethodElement(mb.build()));
    }

    protected InterceptionElement generateSuperMethodInterception(ExecutableElement proxyMethodElement) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        codeBlock.add("$N->{", Interceptor.INVOCATION_CONTEXT_PARAM);
        codeBlock.add("\n");
        codeBlock.indent();

        // Process method parameters
        List<? extends VariableElement> methodParams = proxyMethodElement.getParameters();

        if (!methodParams.isEmpty()) {
            codeBlock.addStatement("$T[] p=" + Interceptor.INVOCATION_CONTEXT_PARAM + "." + InvocationContext.GET_PARAMETERS_METHOD + "()", ClassName.get(Object.class));
        }

        List paramItems = new ArrayList();
        List<String> paramFormats = new ArrayList<>();

        int i = 0;
        for (VariableElement paramElm : methodParams) {
            String paramName = "p[" + i + "]";
            paramItems.add(TypeName.get(paramElm.asType()));
            paramItems.add(paramName);
            paramFormats.add("($T)$N");
            i++;
        }

        String methodName = CodegenUtils.getMethodName(proxyMethodElement);
        TypeMirror retType = proxyMethodElement.getReturnType();
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

    protected void generateInvocationContextExec(ExecutableElement methodElement, MethodSpec.Builder proxyMethodBuilder) {
        List<? extends VariableElement> methodParams = methodElement.getParameters();
        List<String> paramNames = new ArrayList<>();
        for (VariableElement paramElm : methodParams) {
            paramNames.add(StrUtils.addPrefix(METHOD_PARAM_PREFIX, paramElm.getSimpleName().toString()));
        }
        String paramsArrayLiteral = "{" + String.join(",", paramNames) + "}";

        proxyMethodBuilder.addStatement("final $T " + INV_CONTEXT_VARIABLE + "=new $T(this,$S,new $T[]$L," + INTERCEPTORS_CHAIN_VARIABLE + ")",
                ClassName.get(InvocationContext.class),
                ClassName.get(InvocationContext.class),
                CodegenUtils.getMethodName(methodElement),
                ArrayTypeName.get(Object.class), paramsArrayLiteral);
        TypeMirror returnType = methodElement.getReturnType();
        boolean voidResult = returnType instanceof NoType;

        if (voidResult) {
            proxyMethodBuilder.addStatement(INV_CONTEXT_VARIABLE + "." + InvocationContext.PROCEED_METHOD + "()");
        } else {
            proxyMethodBuilder.addStatement("return ($T)" + INV_CONTEXT_VARIABLE + "." + InvocationContext.PROCEED_METHOD + "()",
                    TypeName.get(methodElement.getReturnType()));
        }
    }

    protected void generateProxyMethods(ServiceElement serviceElement, TypeSpec.Builder proxyBuilder) {
        for (ProxyMethodElement method : serviceElement.getProxyMethods()) {
            logger.debug("Generate proxy method:" + method.getName() + "; isPlain=" + method.isPlain());

            if (method.isPlain()) {
                continue;
            }

            MethodSpec.Builder proxyMethodBuilder = CodegenUtils.createBuilderProxyMethod(
                    false, method.getOriginMethod(), null, null, METHOD_PARAM_PREFIX, true,true);

            proxyMethodBuilder.addStatement("final $T " + INTERCEPTORS_CHAIN_VARIABLE + "= new $T()",
                    ClassName.get(InterceptorsChain.class),
                    ClassName.get(InterceptorsChain.class));

            // Adds interceptors code for each interception  phase
            for (String intrcPhase : context.getInterceptionPhases().getPhaseOrder()) {
                for (InterceptionElement intrcBinding : method.getPhaseInterceptions(intrcPhase)) {
                    generateInterceptorBindings(intrcBinding, proxyMethodBuilder);
                }
            }

            // Super method call
            if (method.getSuperMethodInterception() != null) {
                generateInterceptorBindings(method.getSuperMethodInterception(), proxyMethodBuilder);
            } else {
                InterceptionElement ihe = generateSuperMethodInterception(method.getOriginMethod());
                generateInterceptorBindings(ihe, proxyMethodBuilder);
            }

            // Add invContext.proceed...
            generateInvocationContextExec(method.getOriginMethod(), proxyMethodBuilder);
            proxyBuilder.addMethod(proxyMethodBuilder.build());
        }
    }

    private void generateInterceptorBindings(InterceptionElement interceptionElement, MethodSpec.Builder proxyMethodBuilder) {
        CodeBlock.Builder interceptorBindings = CodeBlock.builder();
        interceptorBindings.add(INTERCEPTORS_CHAIN_VARIABLE + ".add(");
        interceptorBindings.add(interceptionElement.getInterceptor());
        interceptorBindings.add(",");

        if (interceptionElement.getParameters() != null) {
            interceptorBindings.add(interceptionElement.getParameters());
        } else {
            interceptorBindings.add("null");
        }
        interceptorBindings.add(");\n");
        proxyMethodBuilder.addCode(interceptorBindings.build());
    }

    public void generateService(ServiceElement serviceElement) {

        TypeSpec.Builder proxyBuilder = TypeSpec.classBuilder(serviceElement.getProxyClassSimpleName()).superclass(ClassName.get(serviceElement.getOriginClass()));
        proxyBuilder.addSuperinterface(ClassName.get(ServicePrototype.class));
        proxyBuilder.addModifiers(Modifier.PUBLIC);
        proxyBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.buildGenstampAnnotation(this.getClass().getName(),null, "Origin: " + serviceElement.getOriginClass().getQualifiedName().toString());
        proxyBuilder.addAnnotation(genstamp);

        AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.get(Unscoped.class));
        proxyBuilder.addAnnotation(scopeAnnBuilder.build());

        // Extra interfaces
        for (TypeName extraInterface : serviceElement.getInterfaces()) {
            proxyBuilder.addSuperinterface(extraInterface);
        }

        // Generate class fields
        for (FieldElement fh : serviceElement.getFields()) {
            proxyBuilder.addField(fh.getSpec());
        }

        // Proxy constuctor
        generateProxyConstructor(serviceElement, proxyBuilder);

        generateGetSuperClassMethod(serviceElement);

        // Generate proxy class methods
        generateProxyMethods(serviceElement, proxyBuilder);

        // Custom methods
        for (CustomMethodElement customMethod : serviceElement.getCustomMethods()) {
            proxyBuilder.addMethod(customMethod.getSpec());
        }

        // Build class file
        TypeElement originClass = serviceElement.getOriginClass();
        final TypeSpec typeSpec = proxyBuilder.build();
        String packageName = CodegenUtils.getPackageName(originClass);
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, originClass);

        teleFacadesGenerator.generateTeleFacades(serviceElement);

        context.getModulatorKit().notifyServiceGenerated(serviceElement);
    }
}
