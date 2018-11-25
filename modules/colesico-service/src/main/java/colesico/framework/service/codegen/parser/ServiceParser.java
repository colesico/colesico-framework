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

    protected DeclaredType getServiceScope(TypeElement serviceType) {
        List<? extends AnnotationMirror> annMirors = serviceType.getAnnotationMirrors();
        DeclaredType scopeAnnotation = null;
        for (AnnotationMirror annMirr : annMirors) {
            DeclaredType annType = annMirr.getAnnotationType();
            CustomScope customScope = annType.getAnnotation(CustomScope.class);
            if (customScope != null) {
                if (scopeAnnotation != null) {
                    throw CodegenException.of().message("Ambiguous scope declaration").element(serviceType).create();
                } else {
                    scopeAnnotation = annType;
                }
            }
        }

        return scopeAnnotation;
    }

    protected void addProxyMethods(ServiceElement serviceElement) {
        TypeElement classElement = serviceElement.getOriginClass();
        List<ExecutableElement> methods = CodegenUtils.getProxiableMethods(
                context.getProcessingEnv(),
                classElement,
                new Modifier[]{Modifier.PUBLIC});

        PlainMethod classPlain = classElement.getAnnotation(PlainMethod.class);
        LocalMethod classLocal = classElement.getAnnotation(LocalMethod.class);
        for (ExecutableElement method : methods) {

            PlainMethod methodPlain = method.getAnnotation(PlainMethod.class);
            final boolean isPlain = classPlain != null || methodPlain != null;

            LocalMethod methodLocal = method.getAnnotation(LocalMethod.class);
            boolean isLocal = methodLocal != null || classLocal != null;

            ProxyMethodElement proxyMethod = new ProxyMethodElement(method, isPlain, isLocal);
            serviceElement.addProxyMethod(proxyMethod);

            context.getModulatorKit().notifyProxyMethod(proxyMethod);
        }
    }

    public ServiceElement parse(TypeElement serviceType) {
        try {
            ServiceElement service = new ServiceElement(serviceType, getServiceScope(serviceType));
            context.getModulatorKit().notifyService(service);

            addProxyMethods(service);

            context.getModulatorKit().notifyAddTeleFacade(service);
            teleFacadesParser.parseTeleFacades(service);

            context.getModulatorKit().notifyServiceParsed(service);
            return service;
        } catch (CodegenException ce) {
            throw ce;
        } catch (Exception ex) {
            throw CodegenException.of().cause(ex).element(serviceType).create();
        }
    }


}
