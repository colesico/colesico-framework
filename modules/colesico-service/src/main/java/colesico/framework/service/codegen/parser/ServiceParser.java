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

import colesico.framework.assist.codegen.model.*;
import colesico.framework.ioc.CustomScope;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.LocalMethod;
import colesico.framework.service.PlainMethod;
import colesico.framework.service.codegen.model.ProxyMethodElement;
import colesico.framework.service.codegen.model.ServiceElement;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class ServiceParser {

    protected final ProcessorContext context;

    protected final TeleFacadesParser teleFacadesParser;

    public ServiceParser(ProcessorContext context) {
        this.context = context;
        this.teleFacadesParser = new TeleFacadesParser(context);
    }

    protected ClassType getServiceScope(ClassElement serviceElement) {
        List<AnnotationMirrorElement> annMirors = serviceElement.getAnnotationMirrors();
        DeclaredType scopeType = null;
        for (AnnotationMirrorElement annMirr : annMirors) {
            DeclaredType annType = annMirr.getType();
            CustomScope customScope = annType.getAnnotation(CustomScope.class);
            if (customScope != null) {
                if (scopeType != null) {
                    throw CodegenException.of().message("Ambiguous scope declaration").element(serviceElement).build();
                } else {
                    scopeType = annType;
                }
            }
        }
        return scopeType==null? null: new ClassType(context.getProcessingEnv(), scopeType);
    }

    protected void addProxyMethods(ServiceElement serviceElement) {
        ClassElement classElement = serviceElement.getOriginClass();
        List<MethodElement> methods = classElement.getMethodsFiltered(
                m -> m.unwrap().getModifiers().contains(Modifier.PUBLIC) & !m.unwrap().getModifiers().contains(Modifier.FINAL)
        );

        AnnotationElement<PlainMethod> classPlain = classElement.getAnnotation(PlainMethod.class);
        AnnotationElement<LocalMethod> classLocal = classElement.getAnnotation(LocalMethod.class);
        for (MethodElement method : methods) {

            AnnotationElement<PlainMethod> methodPlain = method.getAnnotation(PlainMethod.class);
            final boolean isPlain = classPlain != null || methodPlain != null;

            AnnotationElement<LocalMethod> methodLocal = method.getAnnotation(LocalMethod.class);
            boolean isLocal = methodLocal != null || classLocal != null;

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
            throw ce;
        } catch (Exception ex) {
            throw CodegenException.of().cause(ex).element(serviceTypeElement).build();
        }
    }


}
