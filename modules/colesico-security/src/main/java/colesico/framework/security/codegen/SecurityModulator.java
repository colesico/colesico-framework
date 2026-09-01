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

package colesico.framework.security.codegen;


import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.security.authentication.Authentication;
import colesico.framework.security.authentication.AuthenticationInterceptor;
import colesico.framework.security.authentication.AuthenticationPolicy;
import colesico.framework.security.authentication.Authentications;
import colesico.framework.security.authorization.RequireIdentity;
import colesico.framework.security.authorization.RequireIdentityAudit;
import colesico.framework.security.authorization.SecurityAudit;
import colesico.framework.service.interception.Interceptor;
import colesico.framework.service.codegen.model.InterceptionElement;
import colesico.framework.service.codegen.model.InterceptionPhases;
import colesico.framework.service.codegen.model.ServiceFieldElement;
import colesico.framework.service.codegen.model.ServiceMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * Generates security audit interceptors
 */
public class SecurityModulator extends Modulator {

    @Override
    public void onServiceMethodParsed(ServiceMethodElement serviceMethod) {
        super.onServiceMethodParsed(serviceMethod);

        if (serviceMethod.isPlain()) {
            return;
        }

        processAuditors();
        processAuthentications();
    }

    private void processAuditors() {
        List<SecurityAuditorElement> auditors = new ArrayList<>();

        retrieveRequireIdentity().ifPresent(auditors::add);
        auditors.addAll(retrieveSecurityAudit());

        int auditorIdx = 0;
        for (SecurityAuditorElement sae : auditors) {
            auditorIdx++;

            // Add auditor field
            String fieldName = StringUtils.firstCharToLowerCase(sae.auditorClass().simpleName()) + auditorIdx;
            FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(sae.auditorClass().originType()), fieldName).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
            ServiceFieldElement fieldElement = new ServiceFieldElement(fieldSpec).inject();
            service.addCustomField(fieldElement);

            // Add interceptor invocation code
            CodeBlock.Builder codeBlock = CodeBlock.builder();
            codeBlock.add("$N::$N", fieldName, Interceptor.INTERCEPT_METHOD);
            serviceMethod.addInterception(InterceptionPhases.AUTHORIZATION, new InterceptionElement(codeBlock.build()));
        }
    }

    private Optional<SecurityAuditorElement> retrieveRequireIdentity() {
        var requireIdentity = serviceMethod.originMethod().annotation(RequireIdentity.class);
        if (requireIdentity == null) {
            requireIdentity = service.originClass().annotation(RequireIdentity.class);
        }
        if (requireIdentity == null) {
            return Optional.empty();
        }
        var se = new SecurityAuditorElement(ClassElement.of(processorContext().processingEnv(), RequireIdentityAudit.class));
        return Optional.of(se);
    }

    private List<SecurityAuditorElement> retrieveSecurityAudit() {
        List<SecurityAuditorElement> result = new ArrayList<>();

        var securityAudit = serviceMethod.originMethod().annotation(SecurityAudit.class);
        if (securityAudit == null) {
            securityAudit = service.originClass().annotation(SecurityAudit.class);
        }
        if (securityAudit == null) {
            return result;
        }

        TypeMirror[] tmArr = securityAudit.valueTypeMirrors(a -> a.value());
        for (TypeMirror tm : tmArr) {
            SecurityAuditorElement se = new SecurityAuditorElement(ClassElement.of(processorContext().processingEnv(), (DeclaredType) tm));
            result.add(se);
        }

        return result;
    }

    protected Set<AnnotationAssist<Authentication>> findMethodAuthentications() {
        final Set<AnnotationAssist<Authentication>> result = new HashSet<>();

        AnnotationAssist<Authentication> auth = serviceMethod.originMethod().annotation(Authentication.class);
        if (auth != null) {
            result.add(auth);
        } else {
            AnnotationAssist<Authentications> auths = serviceMethod.originMethod().annotation(Authentications.class);
            if (auths != null) {
                for (Authentication au : auths.unwrap().value()) {
                    auth = new AnnotationAssist<>(processorContext.processingEnv(), au);
                    result.add(auth);
                }
            }
        }
        return result;
    }

    protected Set<AnnotationAssist<Authentication>> findClassAuthentications() {
        final Set<AnnotationAssist<Authentication>> result = new HashSet<>();

        AnnotationAssist<Authentication> auth = service.originClass().annotation(Authentication.class);
        if (auth != null) {
            result.add(auth);
        } else {
            AnnotationAssist<Authentications> auths = service.originClass().annotation(Authentications.class);
            if (auths != null) {
                for (Authentication au : auths.unwrap().value()) {
                    auth = new AnnotationAssist<>(processorContext.processingEnv(), au);
                    result.add(auth);
                }
            }
        }
        return result;
    }

    private void processAuthentications() {

        Set<AnnotationAssist<Authentication>> authentications = findMethodAuthentications();
        if (authentications.isEmpty()) {
            authentications = findClassAuthentications();
        }

        if (authentications.isEmpty()) {
            return;
        }

        AnnotationAssist<AuthenticationPolicy> authenticationPolicy = serviceMethod.originMethod().annotation(AuthenticationPolicy.class);
        if (authenticationPolicy == null) {
            authenticationPolicy = service.originClass().annotation(AuthenticationPolicy.class);
        }

        var strategy = AuthenticationPolicy.Strategy.IF_NECESSARY.name();
        if (authenticationPolicy != null) {
            strategy = authenticationPolicy.unwrap().value().name();
        }

        // Add authentication interceptor field
        String fieldName = StringUtils.firstCharToLowerCase(AuthenticationInterceptor.class.getSimpleName());
        FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(AuthenticationInterceptor.class), fieldName).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
        ServiceFieldElement fieldElement = new ServiceFieldElement(fieldSpec).inject();
        service.addCustomField(fieldElement);

        // Sources classes code
        CodeBlock.Builder paramsCode = CodeBlock.builder();
        paramsCode.add("new $T(", ClassName.get(AuthenticationInterceptor.Options.class));
        ArrayCodegen paramsCodegen = new ArrayCodegen(ClassName.get(Class.class));
        for (var authentication : authentications) {
            TypeMirror authenticatorClass = authentication.valueTypeMirror(a -> a.value());
            paramsCodegen.add("$T.class", TypeName.get(authenticatorClass));
        }
        paramsCode.add(paramsCodegen.toFormat(), paramsCodegen.toValues());


        paramsCode.add(",$T.$L)", ClassName.get(AuthenticationPolicy.Strategy.class), strategy);

        // Add interceptor invocation code
        CodeBlock.Builder interceptorCode = CodeBlock.builder();
        interceptorCode.add("$N::$N", fieldName, Interceptor.INTERCEPT_METHOD);
        serviceMethod.addInterception(InterceptionPhases.AUTHENTICATION,
                new InterceptionElement(interceptorCode.build(), paramsCode.build()));
    }
}
