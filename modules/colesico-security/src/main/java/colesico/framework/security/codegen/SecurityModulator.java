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

package colesico.framework.security.codegen;


import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.security.RequireAuthority;
import colesico.framework.security.RequirePrincipal;
import colesico.framework.security.SecurityInterceptor;
import colesico.framework.security.SecurityKit;
import colesico.framework.service.codegen.model.FieldElement;
import colesico.framework.service.codegen.model.InterceptionElement;
import colesico.framework.service.codegen.model.InterceptionPhases;
import colesico.framework.service.codegen.model.ProxyMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

/**
 * @author Vladlen Larionov
 */
@AutoService(Modulator.class)
public class SecurityModulator extends Modulator {

    public static final String SEQURITY_KIT_FIELD = "securityKit";

    @Override
    public void onProxyMethod(ProxyMethodElement proxyMethod) {
        super.onProxyMethod(proxyMethod);
        final RequirePrincipal requirePrincipal = proxyMethod.getOriginMethod().getAnnotation(RequirePrincipal.class);
        final RequireAuthority requireAuthority = proxyMethod.getOriginMethod().getAnnotation(RequireAuthority.class);

        if (requirePrincipal != null || requireAuthority != null) {
            if (proxyMethod.isPlain()) {
                throw CodegenException.of().message("To use @" + RequirePrincipal.class.getSimpleName() + " or @"
                        + RequireAuthority.class.getSimpleName() + " method should not be plain method").element(proxyMethod.getOriginMethod()).build();
            }
        } else {
            return;
        }

        FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(SecurityKit.class), SEQURITY_KIT_FIELD).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
        FieldElement fieldElement = new FieldElement(fieldSpec, true, null);
        service.addField(fieldElement);

        if (requireAuthority != null) {

            CodeBlock.Builder paramCode = CodeBlock.builder();
            paramCode.add("new $T[]{", ClassName.get(String.class));
            String[] authorityIds = requireAuthority.value();
            ArrayCodegen ac = new ArrayCodegen();
            for (String authorityId : authorityIds) {
                ac.add("$S", authorityId);
            }
            paramCode.add(ac.toFormat(), ac.toValues());
            paramCode.add("}");

            CodeBlock.Builder handlerCode = CodeBlock.builder();
            handlerCode.add("(($T)$N)::$N", ClassName.get(SecurityInterceptor.class) , SEQURITY_KIT_FIELD, SecurityInterceptor.INTERCEPT_REQUIRE_AUTHORITY_METHOD);
            proxyMethod.addInterception(InterceptionPhases.AUTHORIZATION,new InterceptionElement(handlerCode.build(),paramCode.build()));
        } else if (requirePrincipal != null) {
            CodeBlock.Builder codeBlock = CodeBlock.builder();
            codeBlock.add("(($T)$N)::$N", ClassName.get(SecurityInterceptor.class) , SEQURITY_KIT_FIELD, SecurityInterceptor.INTERCEPT_REQUIRE_PRINCIPAL_METHOD);
            proxyMethod.addInterception(InterceptionPhases.AUTHORIZATION,new InterceptionElement(codeBlock.build()));
        } else {
            throw CodegenException.of().message("Security modulator has confused :)").element(proxyMethod.getOriginMethod()).build();
        }

    }

}
