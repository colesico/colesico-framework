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

package colesico.framework.security.codegen;


import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.security.*;
import colesico.framework.service.codegen.model.InterceptionElement;
import colesico.framework.service.codegen.model.InterceptionPhases;
import colesico.framework.service.codegen.model.ProxyFieldElement;
import colesico.framework.service.codegen.model.ProxyMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class SecurityModulator extends Modulator {

    @Override
    public void onProxyMethodCreated(ProxyMethodElement proxyMethod) {
        super.onProxyMethodCreated(proxyMethod);
        final AnnotationAssist<RequirePrincipal> requirePrincipal = proxyMethod.getOriginMethod().getAnnotation(RequirePrincipal.class);
        final AnnotationAssist<SecurityAudit> securityAudit = proxyMethod.getOriginMethod().getAnnotation(SecurityAudit.class);

        if (requirePrincipal == null && securityAudit == null) {
            return;
        }

        if (proxyMethod.isPlain()) {
            throw CodegenException.of().message("To use @" + RequirePrincipal.class.getSimpleName() + " or @"
                    + SecurityAudit.class.getSimpleName() + " method should not be plain method").element(proxyMethod.getOriginMethod()).build();
        }

        List<SecurityAuditorElement> auditors = new ArrayList<>();

        if (requirePrincipal != null) {
            SecurityAuditorElement se = new SecurityAuditorElement(ClassElement.fromClass(getProcessorContext().getProcessingEnv(), RequirePrincipalAudit.class));
            auditors.add(se);
        }

        if (securityAudit != null) {
            TypeMirror[] tmArr = securityAudit.getValueTypeMirrors(a -> a.value());
            for (TypeMirror tm : tmArr) {
                SecurityAuditorElement se = new SecurityAuditorElement(ClassElement.fromType(getProcessorContext().getProcessingEnv(), (DeclaredType) tm));
                auditors.add(se);
            }
        }

        int auditorIdx = 0;
        for (SecurityAuditorElement sae : auditors) {
            auditorIdx++;

            // Add auditor field
            String fieldName = StrUtils.firstCharToLowerCase(sae.getAuditorClass().getSimpleName()) + auditorIdx;
            FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(sae.getAuditorClass().getOriginType()), fieldName).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
            ProxyFieldElement fieldElement = new ProxyFieldElement(fieldSpec).inject();
            service.addField(fieldElement);

            // Add interceptor invocation code
            CodeBlock.Builder codeBlock = CodeBlock.builder();
            codeBlock.add("$N::$N", fieldName, AuditInterceptor.AUDIT_METHOD);
            proxyMethod.addInterception(InterceptionPhases.AUTHORIZATION, new InterceptionElement(codeBlock.build()));
        }
    }
}
