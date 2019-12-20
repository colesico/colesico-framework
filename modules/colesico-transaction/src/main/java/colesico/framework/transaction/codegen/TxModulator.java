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

package colesico.framework.transaction.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.service.Interceptor;
import colesico.framework.service.InvocationContext;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.transaction.TransactionPropagation;
import colesico.framework.transaction.Transactional;
import colesico.framework.transaction.TransactionalShell;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;

public class TxModulator extends Modulator {

    public static final String TX_SHELL_FIELD_PREFIX = "txShell";

    @Override
    public void onService(ServiceElement service) {
        super.onService(service);
        TxModulatorContext context = new TxModulatorContext();
        service.setProperty(context);
    }

    protected TxModulatorContext getModulatorContext() {
        return getService().getProperty(TxModulatorContext.class);
    }

    protected String getPropogationMethodName(TransactionPropagation propogation) {
        switch (propogation) {
            case REQUIRED:
                return TransactionalShell.REQUIRED_METHOD;
            case REQUIRES_NEW:
                return TransactionalShell.REQUIRES_NEW_METHOD;
            case MANDATORY:
                return TransactionalShell.MANDATORY_METHOD;
            case NOT_SUPPORTED:
                return TransactionalShell.NOT_SUPPORTED_METHOD;
            case SUPPORTS:
                return TransactionalShell.SUPPORTS_METHOD;
            case NEVER:
                return TransactionalShell.NEVER_METHOD;
            case NESTED:
                return TransactionalShell.NESTED_METHOD;
            default:
                throw CodegenException.of().message("Unsupported transaction propogateion:" + propogation.name()).build();
        }
    }

    @Override
    public void onProxyMethod(ProxyMethodElement proxyMethod) {
        super.onProxyMethod(proxyMethod);

        AnnotationElement<Transactional> txAnnotation = proxyMethod.getOriginMethod().getAnnotation(Transactional.class);
        if (txAnnotation == null) {
            txAnnotation = proxyMethod.getParentService().getOriginClass().getAnnotation(Transactional.class);
            if (txAnnotation == null) {
                return;
            }
        }

        // Add shell field

        TxModulatorContext ctx = getModulatorContext();
        Integer exIdx = ctx.getShellIndex(txAnnotation.unwrap().shell());
        String shellFieldName = TX_SHELL_FIELD_PREFIX + exIdx;

        FieldSpec txShellFs = FieldSpec.builder(ClassName.get(TransactionalShell.class), shellFieldName).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
        ProxyFieldElement txShellFe = new ProxyFieldElement(txShellFs).inject();
        if (StringUtils.isNotEmpty(txAnnotation.unwrap().shell())) {
            txShellFe.setNamed(txAnnotation.unwrap().shell());
        }
        proxyMethod.getParentService().addField(txShellFe);

        // Add interceptor

        String propogationMethodName = getPropogationMethodName(txAnnotation.unwrap().propagation());

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("$N->", Interceptor.INVOCATION_CONTEXT_PARAM);
        //cb.add("$N.$N(()->$N.$N(),null)",
        cb.add("$N.$N($N::$N,null)",
                shellFieldName,
                propogationMethodName,
                Interceptor.INVOCATION_CONTEXT_PARAM,
                InvocationContext.PROCEED_METHOD
        );

        InterceptionElement interception = new InterceptionElement(cb.build());
        proxyMethod.addInterception(InterceptionPhases.TRANSACTION, interception);
    }
}
