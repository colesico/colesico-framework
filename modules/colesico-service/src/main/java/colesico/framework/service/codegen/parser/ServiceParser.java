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

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.listener.PostConstruct;
import colesico.framework.ioc.scope.CustomScope;
import colesico.framework.service.InjectParam;
import colesico.framework.service.LocalMethod;
import colesico.framework.service.PlainMethod;
import colesico.framework.service.ServiceMethod;
import colesico.framework.service.codegen.model.ServiceInjectParamElement;
import colesico.framework.service.codegen.model.ServiceMethodElement;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.ServiceParameterElement;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class ServiceParser extends FrameworkAbstractParser {

    protected final ServiceProcessorContext context;

    protected final TeleFacadeParser teleFacadeParser;

    public ServiceParser(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
        this.teleFacadeParser = new TeleFacadeParser(context);
    }

    protected ClassType getServiceScope(ClassElement serviceElement) {
        List<AnnotationType> allAnnotations = serviceElement.annotationTypes();
        DeclaredType scopeType = null;
        for (AnnotationType annotation : allAnnotations) {
            AnnotationElement annotationElement = annotation.asElement();

            // Check is annotation annotated with @CustomScope
            AnnotationAssist<CustomScope> customScope = annotationElement.annotation(CustomScope.class);
            if (customScope != null) {
                if (scopeType != null) {
                    throw CodegenException.of().message("Ambiguous scope declaration").element(serviceElement).build();
                } else {
                    scopeType = (DeclaredType) annotation.unwrap();
                }
            }
        }
        return scopeType == null ? null : new ClassType(context.processingEnv(), scopeType);
    }


    protected boolean isPlainMethod(MethodElement m, ClassElement classElement) {
        AnnotationAssist<PlainMethod> classPlainAnn = classElement.annotation(PlainMethod.class);
        AnnotationAssist<ServiceMethod> classServAnn = classElement.annotation(ServiceMethod.class);
        List<AnnotationAssist<PlainMethod>> plainMethodAnns = m.annotationsInherited(PlainMethod.class);
        AnnotationAssist<ServiceMethod> serviceMethodAnn = m.annotation(ServiceMethod.class);

        final boolean isFinal = m.unwrap().getModifiers().contains(Modifier.FINAL);
        final boolean isPublic = m.unwrap().getModifiers().contains(Modifier.PUBLIC);
        final boolean isPrivate = m.unwrap().getModifiers().contains(Modifier.PRIVATE);
        final boolean isServ = serviceMethodAnn != null;
        final boolean isPlain = !plainMethodAnns.isEmpty();

        if (isServ) {
            return false;
        }

        if (isPlain || isFinal || isPrivate) {
            return true;
        }

        if (classPlainAnn != null) {
            return true;
        }

        if (classServAnn != null) {
            return false;
        }

        return !isPublic;
    }

    protected void parseMethodParams(ServiceMethodElement serviceMethod) {

        List<ParameterElement> methodParams = serviceMethod.originMethod().parameters();
        ServiceParameterElement srvParam;

        // Process method parameters
        for (var param : methodParams) {
            AnnotationAssist<InjectParam> injectParamAnn = param.annotation(InjectParam.class);
            if (injectParamAnn != null) {
                var sipe = new ServiceInjectParamElement(serviceMethod, param);
                srvParam = sipe;

                String named = injectParamAnn.unwrap().named();
                sipe.setNamed(StrUtils.isEmpty(named) ? null : named);

                TypeMirror classed = injectParamAnn.valueTypeMirror(InjectParam::classed);
                if (!CodegenUtils.isAssignable(Class.class, classed, processingEnv)) {
                    sipe.setClassed(classed);
                }
            } else {
                srvParam = new ServiceParameterElement(serviceMethod, param);
            }

            serviceMethod.addParameter(srvParam);
            context.modulatorKit().notifyServiceParameterParsed(srvParam);
        }
    }

    protected void parseServiceMethods(ServiceElement serviceElement) {
        ClassElement classElement = serviceElement.originClass();
        AnnotationAssist<LocalMethod> classLocalAnn = classElement.annotation(LocalMethod.class);

        List<MethodElement> methods = classElement.methods();

        for (MethodElement method : methods) {
            if (method.unwrap().getModifiers().contains(Modifier.STATIC)) {
                logger.debug("Skip static method: {}", method.name());
                continue;
            }

            // is plain ?
            boolean isPlain = isPlainMethod(method, classElement);

            // is local?
            AnnotationAssist<LocalMethod> localAnn = method.annotation(LocalMethod.class);
            boolean isLocal = localAnn != null || classLocalAnn != null
                    || !method.unwrap().getModifiers().contains(Modifier.PUBLIC);

            // is post construct listener?
            AnnotationAssist<PostConstruct> pcListenerAnn = method.annotation(PostConstruct.class);
            boolean isPCListener = pcListenerAnn != null &&
                    method.unwrap().getModifiers().contains(Modifier.PUBLIC);

            ServiceMethodElement serviceMethod = new ServiceMethodElement(method, isPlain, isLocal, isPCListener);
            serviceElement.addServiceMethod(serviceMethod);

            parseMethodParams(serviceMethod);

            context.modulatorKit().notifyServiceMethodParsed(serviceMethod);
        }
    }

    public ServiceElement parse(TypeElement serviceType) {

        ClassElement serviceClass = ClassElement.of(context.processingEnv(), serviceType);

        ServiceElement service = new ServiceElement(serviceClass, getServiceScope(serviceClass));

        context.modulatorKit().notifyBeforeParseService(service);
        parseServiceMethods(service);
        teleFacadeParser.parse(service);
        context.modulatorKit().notifyServiceParsed(service);

        return service;
    }


}
