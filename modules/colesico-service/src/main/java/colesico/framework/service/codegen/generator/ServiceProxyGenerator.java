/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Unscoped;
import colesico.framework.service.*;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ProcessorContext;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

import static colesico.framework.service.ServiceProxy.GET_SERVICE_ORIGIN_METHOD;

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

    /**
     * Finds the constructor that be used for injection
     *
     * @return
     */
    protected MethodElement findInjectableConstructor(ServiceElement serviceElement) {

        List<MethodElement> constructors = serviceElement.getOriginClass().getConstructorsFiltered(
            c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC) & !c.unwrap().getModifiers().contains(Modifier.FINAL)
        );
        MethodElement constructor = null;
        MethodElement firstConstructor = null;
        for (MethodElement method : constructors) {
            if (firstConstructor == null) {
                firstConstructor = method;
            }
            AnnotationElement<Inject> constructMarker = method.getAnnotation(Inject.class);
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
     *
     * @param fieldElm
     * @param constructorParams
     * @return
     */
    protected ParameterElement findSimilarParam(ProxyFieldElement fieldElm, List<ParameterElement> constructorParams) {

        ParameterElement similarParam = null;
        String fieldNamedVal = fieldElm.getNamed() != null ? fieldElm.getNamed() : "";
        TypeName fieldClassedVal = fieldElm.getClassed() != null ? fieldElm.getClassed() : ClassName.get(Object.class);

        for (ParameterElement paramElm : constructorParams) {
            TypeName paramType = TypeName.get(paramElm.asType());
            AnnotationElement<Named> namedAnn = paramElm.getAnnotation(Named.class);
            AnnotationElement<Classed> classedAnn = paramElm.getAnnotation(Classed.class);

            if (paramType.equals(fieldElm.getInjectAs())) {

                String annNamedVal = namedAnn != null ? namedAnn.unwrap().value() : "";
                TypeName annClassedVal = classedAnn != null ? TypeName.get(classedAnn.getValueTypeMirror(Classed::value))
                    : ClassName.get(Object.class);

                if (annNamedVal.equals(fieldNamedVal) && annClassedVal.equals(fieldClassedVal)) {
                    similarParam = paramElm;
                    break;
                }
            }
        }
        return similarParam;
    }

    protected void generateProxyConstructor(ServiceElement serviceElement, TypeSpec.Builder proxyBuilder) {
        // Find effective constructor
        MethodElement constructor = findInjectableConstructor(serviceElement);


        MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
            constructor, null, METHOD_PARAM_PREFIX, false
        );

        constructorBuilder.addAnnotation(Inject.class);
        CodeBlock sucall = CodegenUtils.generateSuperMethodCall(constructor, null, METHOD_PARAM_PREFIX);
        constructorBuilder.addCode(sucall);

        // Generate extra fields initialization
        List<ParameterElement> constructorParams = constructor.getParameters();
        for (ProxyFieldElement fieldElm : serviceElement.getFields()) {
            if (fieldElm.getInjectAs() == null) {
                continue;
            }

            logger.debug("Generate field initialization:" + fieldElm);

            // Find appropriate existing parameter for field initialization
            ParameterElement similarParam = findSimilarParam(fieldElm, constructorParams);

            if (similarParam == null) {
                ParameterSpec.Builder pb = ParameterSpec.builder(fieldElm.getInjectAs(), fieldElm.getSpec().name);
                if (StringUtils.isNotEmpty(fieldElm.getNamed())) {
                    AnnotationSpec.Builder named = AnnotationSpec.builder(Named.class);
                    named.addMember("value", "$S", fieldElm.getNamed());
                    pb.addAnnotation(named.build());
                } else if (fieldElm.getClassed() != null) {
                    AnnotationSpec.Builder named = AnnotationSpec.builder(Classed.class);
                    named.addMember("value", "$T", fieldElm.getClassed());
                    pb.addAnnotation(named.build());
                }

                constructorBuilder.addParameter(pb.build());
                constructorBuilder.addStatement("this.$N=$N", fieldElm.getSpec().name, fieldElm.getSpec().name);
            } else {
                String paramName = StrUtils.addPrefix(METHOD_PARAM_PREFIX, similarParam.getName());
                constructorBuilder.addStatement("this.$N=$N", fieldElm.getSpec().name, paramName);
            }
        }

        // Generate constructor body extra code
        for (CodeBlock cb : serviceElement.getConstructorExtraCode()) {
            constructorBuilder.addCode(cb);
        }

        proxyBuilder.addMethod(constructorBuilder.build());
    }

    protected void generateGetSuperClassMethod(ServiceElement service) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(GET_SERVICE_ORIGIN_METHOD);
        mb.addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)))
            .addStatement("return $T.class", TypeName.get(service.getOriginClass().asType()));

        service.addCustomMethod(new CustomMethodElement(mb.build()));
    }

    protected InterceptionElement generateSuperMethodInterception(MethodElement proxyMethodElement) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        codeBlock.add("$N->{", Interceptor.INVOCATION_CONTEXT_PARAM);
        codeBlock.add("\n");
        codeBlock.indent();

        // Process method parameters
        List<ParameterElement> methodParams = proxyMethodElement.getParameters();

        if (!methodParams.isEmpty()) {
            codeBlock.addStatement("$T[] p=" + Interceptor.INVOCATION_CONTEXT_PARAM + "." + InvocationContext.GET_PARAMETERS_METHOD + "()", ClassName.get(Object.class));
        }

        List paramItems = new ArrayList();
        List<String> paramFormats = new ArrayList<>();

        int i = 0;
        for (ParameterElement paramElm : methodParams) {
            String paramName = "p[" + i + "]";
            paramItems.add(TypeName.get(paramElm.asType()));
            paramItems.add(paramName);
            paramFormats.add("($T)$N");
            i++;
        }

        String methodName = proxyMethodElement.getName();
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

    protected void generateInvocationContextExec(MethodElement methodElement, MethodSpec.Builder proxyMethodBuilder) {
        List<ParameterElement> methodParams = methodElement.getParameters();
        List<String> paramNames = new ArrayList<>();
        for (ParameterElement paramElm : methodParams) {
            paramNames.add(StrUtils.addPrefix(METHOD_PARAM_PREFIX, paramElm.getName()));
        }
        String paramsArrayLiteral = "{" + String.join(",", paramNames) + "}";

        proxyMethodBuilder.addStatement("final $T " + INV_CONTEXT_VARIABLE + "=new $T(this,$S,new $T[]$L," + INTERCEPTORS_CHAIN_VARIABLE + ")",
            ClassName.get(InvocationContext.class),
            ClassName.get(InvocationContext.class),
            methodElement.getName(),
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
            logger.debug("Generate proxy method:" + method.getName() + "; isPlain=" + method.isPlain() + "; isLocal=" + method.isLocal());

            if (method.isPlain()) {
                continue;
            }

            MethodSpec.Builder proxyMethodBuilder = CodegenUtils.createProxyMethodBuilder(
                method.getOriginMethod(), null, METHOD_PARAM_PREFIX, true
            );

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

        TypeSpec.Builder proxyBuilder = TypeSpec.classBuilder(serviceElement.getProxyClassSimpleName()).superclass(TypeName.get(serviceElement.getOriginClass().asType()));
        proxyBuilder.addSuperinterface(ClassName.get(ServiceProxy.class));
        proxyBuilder.addModifiers(Modifier.PUBLIC);
        proxyBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Origin: " + serviceElement.getOriginClass().unwrap().getQualifiedName().toString());
        proxyBuilder.addAnnotation(genstamp);

        AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.get(Unscoped.class));
        proxyBuilder.addAnnotation(scopeAnnBuilder.build());

        AnnotationSpec.Builder originClassAnnBuilder = AnnotationSpec.builder(ClassName.get(ServiceOrigin.class));
        originClassAnnBuilder.addMember("value","$T.class",TypeName.get(serviceElement.getOriginClass().asType()));
        proxyBuilder.addAnnotation(originClassAnnBuilder.build());

        // Extra interfaces
        for (TypeName extraInterface : serviceElement.getInterfaces()) {
            proxyBuilder.addSuperinterface(extraInterface);
        }

        // Generate class fields
        for (ProxyFieldElement fh : serviceElement.getFields()) {
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
        ClassElement originClass = serviceElement.getOriginClass();
        final TypeSpec typeSpec = proxyBuilder.build();
        String packageName = originClass.getPackageName();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, originClass.unwrap());

        teleFacadesGenerator.generateTeleFacades(serviceElement);

        context.getModulatorKit().notifyServiceGenerated(serviceElement);
    }
}
