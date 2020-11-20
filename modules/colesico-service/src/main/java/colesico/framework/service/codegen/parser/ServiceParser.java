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

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.listener.PostConstruct;
import colesico.framework.ioc.scope.CustomScope;
import colesico.framework.service.LocalMethod;
import colesico.framework.service.PlainMethod;
import colesico.framework.service.ServiceMethod;
import colesico.framework.service.codegen.model.ServiceMethodElement;
import colesico.framework.service.codegen.model.ServiceElement;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class ServiceParser extends FrameworkAbstractParser {

    protected final ServiceProcessorContext context;

    protected final TeleFacadeParser teleFacadeParser;

    public ServiceParser(ServiceProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
        this.teleFacadeParser = new TeleFacadeParser(context);
    }

    protected ClassType getServiceScope(ClassElement serviceElement) {
        List<AnnotationType> allAnnotations = serviceElement.getAnnotationTypes();
        DeclaredType scopeType = null;
        for (AnnotationType annotation : allAnnotations) {
            AnnotationElement annotationElement = annotation.asElement();

            // Check is annotation annotated with @CustomScope
            AnnotationAssist<CustomScope> customScope = annotationElement.getAnnotation(CustomScope.class);
            if (customScope != null) {
                if (scopeType != null) {
                    throw CodegenException.of().message("Ambiguous scope declaration").element(serviceElement).build();
                } else {
                    scopeType = (DeclaredType) annotation.unwrap();
                }
            }
        }
        return scopeType == null ? null : new ClassType(context.getProcessingEnv(), scopeType);
    }


    protected boolean isPlainMethod(MethodElement m, ClassElement classElement) {
        AnnotationAssist<PlainMethod> classPlainAnn = classElement.getAnnotation(PlainMethod.class);
        AnnotationAssist<ServiceMethod> classServAnn = classElement.getAnnotation(ServiceMethod.class);
        List<AnnotationAssist<PlainMethod>> plainMethodAnns = m.getAnnotationsInherited(PlainMethod.class);
        AnnotationAssist<ServiceMethod> serviceMethodAnn = m.getAnnotation(ServiceMethod.class);

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

        if (isPublic) {
            return false;
        }

        return true;
    }

    protected void parseServiceMethods(ServiceElement serviceElement) {
        ClassElement classElement = serviceElement.getOriginClass();
        AnnotationAssist<LocalMethod> classLocalAnn = classElement.getAnnotation(LocalMethod.class);

        List<MethodElement> methods = classElement.getMethods();

        for (MethodElement method : methods) {
            if (method.unwrap().getModifiers().contains(Modifier.STATIC)) {
                logger.debug("Skip static method: {}", method.getName());
                continue;
            }

            // is plain ?
            if (isPlainMethod(method, classElement)) {
                logger.debug("Skip plain method: {}", method.getName());
                continue;
            }

            // is local?
            AnnotationAssist<LocalMethod> localAnn = method.getAnnotation(LocalMethod.class);
            boolean isLocal = localAnn != null || classLocalAnn != null
                    || !method.unwrap().getModifiers().contains(Modifier.PUBLIC);

            // is post construct listener?
            AnnotationAssist<PostConstruct> pcListenerAnn = method.getAnnotation(PostConstruct.class);
            boolean isPCListener = pcListenerAnn != null &&
                    method.unwrap().getModifiers().contains(Modifier.PUBLIC);

            ServiceMethodElement serviceMethod = new ServiceMethodElement(method, isLocal, isPCListener);
            serviceElement.addServiceMethod(serviceMethod);

            context.getModulatorKit().notifyServiceMethodParsed(serviceMethod);
        }
    }

    public ServiceElement parse(TypeElement serviceType) {

        ClassElement serviceClass = ClassElement.fromElement(context.getProcessingEnv(), serviceType);

        ServiceElement service = new ServiceElement(serviceClass, getServiceScope(serviceClass));
        context.getModulatorKit().notifyBeforeParseService(service);

        parseServiceMethods(service);
        teleFacadeParser.parse(service);

        context.getModulatorKit().notifyServiceParsed(service);
        return service;
    }


}
