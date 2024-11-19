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

import javax.inject.Inject;
import javax.inject.Named;
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
public class ServiceGenerator {
    private Logger logger = LoggerFactory.getLogger(ServiceGenerator.class);

    public static final String METHOD_PARAM_PREFIX = "param";
    public static final String INTERCEPTORS_CHAIN_VARIABLE = "interceptors";
    public static final String INV_CONTEXT_VARIABLE = "ctx";

    protected final ServiceProcessorContext context;
    protected final TeleFacadesGenerator teleFacadesGenerator;

    public ServiceGenerator(ServiceProcessorContext context) {
        this.context = context;
        teleFacadesGenerator = new TeleFacadesGenerator(context);
    }

    /**
     * Finds the constructor that be used for injection
     */
    protected MethodElement findInjectableConstructor(ServiceElement serviceElm) {

        List<MethodElement> constructors = serviceElm.getOriginClass().getConstructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC) && !c.unwrap().getModifiers().contains(Modifier.FINAL)
        );
        MethodElement constructor = null;
        MethodElement firstConstructor = null;
        for (MethodElement method : constructors) {
            if (firstConstructor == null) {
                firstConstructor = method;
            }
            AnnotationAssist<Inject> constructMarker = method.getAnnotation(Inject.class);
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
    protected ParameterElement findSimilarParam(ServiceFieldElement fieldElm, List<ParameterElement> constructorParams) {

        ParameterElement similarParam = null;
        String fieldNamedVal = fieldElm.getNamed() != null ? fieldElm.getNamed() : "";
        TypeName fieldClassedVal = fieldElm.getClassed() != null ? fieldElm.getClassed() : ClassName.get(Object.class);

        for (ParameterElement paramElm : constructorParams) {
            TypeName paramType = TypeName.get(paramElm.getOriginType());
            AnnotationAssist<Named> namedAnn = paramElm.getAnnotation(Named.class);
            AnnotationAssist<Classed> classedAnn = paramElm.getAnnotation(Classed.class);

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

    protected void generateServiceConstructor(ServiceElement serviceElm, TypeSpec.Builder serviceBuilder) {
        // Find effective constructor
        MethodElement constructor = findInjectableConstructor(serviceElm);

        MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                constructor, null, METHOD_PARAM_PREFIX, false
        );

        constructorBuilder.addAnnotation(Inject.class);
        CodeBlock sucall = CodegenUtils.generateSuperMethodCall(constructor, null, METHOD_PARAM_PREFIX);
        constructorBuilder.addCode(sucall);

        // Generate extra fields initialization
        List<ParameterElement> constructorParams = constructor.getParameters();
        for (ServiceFieldElement fieldElm : serviceElm.getCustomFields()) {
            if (fieldElm.getInjectAs() == null) {
                continue;
            }

            logger.debug("Generate field initialization:" + fieldElm);

            // Find appropriate existing parameter for field initialization
            ParameterElement similarParam = findSimilarParam(fieldElm, constructorParams);

            if (similarParam == null) {
                ParameterSpec.Builder pb = ParameterSpec.builder(fieldElm.getInjectAs(), fieldElm.getSpec().name());
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
                constructorBuilder.addStatement("this.$N = $N", fieldElm.getSpec().name(), fieldElm.getSpec().name());
            } else {
                String paramName = StrUtils.addPrefix(METHOD_PARAM_PREFIX, similarParam.getName());
                constructorBuilder.addStatement("this.$N = $N", fieldElm.getSpec().name(), paramName);
            }
        }

        // Generate constructor body extra code
        for (CodeBlock cb : serviceElm.getConstructorExtraCode()) {
            constructorBuilder.addCode(cb);
        }

        serviceBuilder.addMethod(constructorBuilder.build());
    }

    protected void generateGetSuperClassMethod(ServiceElement serviceElm) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(GET_SERVICE_ORIGIN_METHOD);
        mb.addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)))
                .addStatement("return $T.class", TypeName.get(serviceElm.getOriginClass().getOriginType()));

        serviceElm.addCustomMethod(new CustomMethodElement(mb.build()));
    }

    protected InterceptionElement generateSuperMethodInterception(MethodElement serviceMethodElm) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        codeBlock.add("$N->{", Interceptor.INVOCATION_CONTEXT_PARAM);
        codeBlock.add("\n");
        codeBlock.indent();

        // Process method parameters
        List<ParameterElement> methodParams = serviceMethodElm.getParameters();

        if (!methodParams.isEmpty()) {
            codeBlock.addStatement("$T[] p = " + Interceptor.INVOCATION_CONTEXT_PARAM + "." + InvocationContext.GET_PARAMETERS_METHOD + "()", ClassName.get(Object.class));
        }

        List paramItems = new ArrayList();
        List<String> paramFormats = new ArrayList<>();

        int i = 0;
        for (ParameterElement paramElm : methodParams) {
            String paramName = "p[" + i + "]";
            paramItems.add(TypeName.get(paramElm.getOriginType()));
            paramItems.add(paramName);
            paramFormats.add("($T)$N");
            i++;
        }

        String methodName = serviceMethodElm.getName();
        TypeMirror retType = serviceMethodElm.getReturnType();
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
        List<ParameterElement> methodParams = methodElement.getParameters();
        List<String> paramNames = new ArrayList<>();
        for (ParameterElement paramElm : methodParams) {
            paramNames.add(StrUtils.addPrefix(METHOD_PARAM_PREFIX, paramElm.getName()));
        }
        String paramsArrayLiteral = "{" + String.join(",", paramNames) + "}";

        serviceMethodBuilder.addStatement("final $T " + INV_CONTEXT_VARIABLE + "= new $T(this,$S,new $T[]$L," + INTERCEPTORS_CHAIN_VARIABLE + ")",
                ClassName.get(InvocationContext.class),
                ClassName.get(InvocationContext.class),
                methodElement.getName(),
                ArrayTypeName.get(Object.class), paramsArrayLiteral);
        TypeMirror returnType = methodElement.getReturnType();
        boolean voidResult = returnType instanceof NoType;

        if (voidResult) {
            serviceMethodBuilder.addStatement(INV_CONTEXT_VARIABLE + "." + InvocationContext.PROCEED_METHOD + "()");
        } else {
            serviceMethodBuilder.addStatement("return ($T)" + INV_CONTEXT_VARIABLE + "." + InvocationContext.PROCEED_METHOD + "()",
                    TypeName.get(methodElement.getReturnType()));
        }
    }

    protected void generateServiceMethods(ServiceElement serviceElm, TypeSpec.Builder serviceBuilder) {
        for (ServiceMethodElement method : serviceElm.getServiceMethods()) {
            logger.debug("Generate proxy method:" + method.getName() +
                    "; isPlain=" + method.isPlain() +
                    "; isLocal=" + method.isLocal() +
                    "; isPCListener=" + method.isPostConstructListener());

            if (method.isPlain()) {
                logger.debug("Skip plain method: " + method.getName());
                continue;
            }

            MethodSpec.Builder methodBuilder = CodegenUtils.createProxyMethodBuilder(
                    method.getOriginMethod(), null, METHOD_PARAM_PREFIX, true
            );

            if (method.isPostConstructListener()) {
                methodBuilder.addAnnotation(PostConstruct.class);
            }

            methodBuilder.addStatement("final $T " + INTERCEPTORS_CHAIN_VARIABLE + "= new $T()",
                    ClassName.get(InterceptorsChain.class),
                    ClassName.get(InterceptorsChain.class));

            // Adds interceptors code for each interception  phase
            for (String intrcPhase : context.getInterceptionPhases().getPhaseOrder()) {
                for (InterceptionElement intrcBinding : method.getPhaseInterceptions(intrcPhase)) {
                    generateInterceptorBindings(intrcBinding, methodBuilder);
                }
            }

            // Super method call
            if (method.getInterception() != null) {
                generateInterceptorBindings(method.getInterception(), methodBuilder);
            } else {
                InterceptionElement ihe = generateSuperMethodInterception(method.getOriginMethod());
                generateInterceptorBindings(ihe, methodBuilder);
            }

            // Add invContext.proceed...
            generateInvocationContextExec(method.getOriginMethod(), methodBuilder);
            serviceBuilder.addMethod(methodBuilder.build());
        }
    }

    private void generateInterceptorBindings(InterceptionElement interceptionElm, MethodSpec.Builder serviceMethodBuilder) {
        CodeBlock.Builder interceptorBindings = CodeBlock.builder();
        interceptorBindings.add(INTERCEPTORS_CHAIN_VARIABLE + ".add(");
        interceptorBindings.add(interceptionElm.getInterceptorCode());
        interceptorBindings.add(",");

        if (interceptionElm.getParametersCode() != null) {
            interceptorBindings.add(interceptionElm.getParametersCode());
        } else {
            interceptorBindings.add("null");
        }
        interceptorBindings.add(");\n");
        serviceMethodBuilder.addCode(interceptorBindings.build());
    }

    public void generate(ServiceElement serviceElm) {

        TypeSpec.Builder serviceBuilder = TypeSpec.classBuilder(serviceElm.getProxyClassSimpleName()).superclass(TypeName.get(serviceElm.getOriginClass().getOriginType()));
        serviceBuilder.addSuperinterface(ClassName.get(ServiceProxy.class));
        serviceBuilder.addModifiers(Modifier.PUBLIC);
        serviceBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Origin: " + serviceElm.getOriginClass().unwrap().getQualifiedName().toString());
        serviceBuilder.addAnnotation(genstamp);

        AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.get(Unscoped.class));
        serviceBuilder.addAnnotation(scopeAnnBuilder.build());

        AnnotationSpec.Builder originAnnBuilder = AnnotationSpec.builder(ClassName.get(ServiceOrigin.class));
        originAnnBuilder.addMember("value", "$T.class", TypeName.get(serviceElm.getOriginClass().getOriginType()));
        serviceBuilder.addAnnotation(originAnnBuilder.build());

        // Extra interfaces
        for (TypeName extraInterface : serviceElm.getCustomInterfaces()) {
            serviceBuilder.addSuperinterface(extraInterface);
        }

        // Generate class fields
        for (ServiceFieldElement fh : serviceElm.getCustomFields()) {
            serviceBuilder.addField(fh.getSpec());
        }

        // Proxy constructor
        generateServiceConstructor(serviceElm, serviceBuilder);

        generateGetSuperClassMethod(serviceElm);

        // Generate proxy class methods
        generateServiceMethods(serviceElm, serviceBuilder);

        // Custom methods
        for (CustomMethodElement customMethod : serviceElm.getCustomMethods()) {
            serviceBuilder.addMethod(customMethod.getSpec());
        }

        // Build class file
        ClassElement originClass = serviceElm.getOriginClass();
        final TypeSpec typeSpec = serviceBuilder.build();
        String packageName = originClass.getPackageName();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, originClass.unwrap());

        teleFacadesGenerator.generate(serviceElm);

        context.getModulatorKit().notifyServiceGenerated(serviceElm);
    }
}
