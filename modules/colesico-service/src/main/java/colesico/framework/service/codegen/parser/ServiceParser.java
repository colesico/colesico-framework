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

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.CustomScope;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.LocalMethod;
import colesico.framework.service.PlainMethod;
import colesico.framework.service.ServiceMethod;
import colesico.framework.service.codegen.model.ProxyMethodElement;
import colesico.framework.service.codegen.model.ServiceElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.util.List;

public class ServiceParser extends FrameworkAbstractParser {

    protected final ProcessorContext context;

    protected final TeleFacadesParser teleFacadesParser;

    public ServiceParser(ProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
        this.teleFacadesParser = new TeleFacadesParser(context);
    }

    protected ClassType getServiceScope(ClassElement serviceElement) {
        List<AnnotationMirrorElement> annMirorElements = serviceElement.getAnnotationMirrors();
        TypeElement scopeType = null;
        for (AnnotationMirrorElement annMirrorElm : annMirorElements) {
            TypeElement annTypeElm = annMirrorElm.unwrap();
            CustomScope customScope = annTypeElm.getAnnotation(CustomScope.class);
            if (customScope != null) {
                if (scopeType != null) {
                    throw CodegenException.of().message("Ambiguous scope declaration").element(serviceElement).build();
                } else {
                    scopeType = annTypeElm;
                }
            }
        }
        return scopeType == null ? null : new ClassType(context.getProcessingEnv(), scopeType);
    }


    protected boolean isPlainMethod(MethodElement m, ClassElement classElement) {
        AnnotationElement<PlainMethod> classPlainAnn = classElement.getAnnotation(PlainMethod.class);
        AnnotationElement<ServiceMethod> classServAnn = classElement.getAnnotation(ServiceMethod.class);
        AnnotationElement<PlainMethod> plainMethodAnn = m.getAnnotation(PlainMethod.class);
        AnnotationElement<ServiceMethod> serviceMethodAnn = m.getAnnotation(ServiceMethod.class);

        final boolean isFinal = m.unwrap().getModifiers().contains(Modifier.FINAL);
        final boolean isPublic = m.unwrap().getModifiers().contains(Modifier.PUBLIC);
        final boolean isProtected = m.unwrap().getModifiers().contains(Modifier.PROTECTED);
        final boolean isPrivate = m.unwrap().getModifiers().contains(Modifier.PRIVATE);
        final boolean isPlain = plainMethodAnn != null;
        final boolean isServ = serviceMethodAnn != null;

        if (isPlain || isFinal || isPrivate) {
            return true;
        }

        if (isServ) {
            return false;
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

    protected void addProxyMethods(ServiceElement serviceElement) {
        ClassElement classElement = serviceElement.getOriginClass();
        AnnotationElement<LocalMethod> classLocalAnn = classElement.getAnnotation(LocalMethod.class);

        List<MethodElement> methods = classElement.getMethods();

        for (MethodElement method : methods) {
            boolean isPlain = isPlainMethod(method, classElement);

            AnnotationElement<LocalMethod> methodLocal = method.getAnnotation(LocalMethod.class);
            boolean isLocal = methodLocal != null || classLocalAnn != null
                || !method.unwrap().getModifiers().contains(Modifier.PUBLIC);

            ProxyMethodElement proxyMethod = new ProxyMethodElement(method, isPlain, isLocal);
            serviceElement.addProxyMethod(proxyMethod);

            context.getModulatorKit().notifyProxyMethod(proxyMethod);
        }
    }

    public ServiceElement parse(TypeElement serviceTypeElement) {
        try {
            ClassElement serviceClassElement = new ClassElement(context.getProcessingEnv(), serviceTypeElement);
            ServiceElement service = new ServiceElement(serviceClassElement, getServiceScope(serviceClassElement));
            context.getModulatorKit().notifyService(service);

            addProxyMethods(service);

            context.getModulatorKit().notifyAddTeleFacade(service);
            teleFacadesParser.parseTeleFacades(service);

            context.getModulatorKit().notifyServiceParsed(service);
            return service;
        } catch (CodegenException ce) {
            ce.print(processingEnv, serviceTypeElement);
            throw ce;
        } catch (Exception e) {
            String msg = ExceptionUtils.getRootCauseMessage(e);
            logger.error("Error parsing service class: " + serviceTypeElement + "; message: " + msg);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }


}
