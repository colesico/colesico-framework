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

package colesico.framework.transaction.codegen;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.interception.Interceptor;
import colesico.framework.service.interception.InvocationContext;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.transaction.TransactionPropagation;
import colesico.framework.transaction.Transactional;
import colesico.framework.transaction.TransactionManager;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

public class TxModulator extends Modulator {

    public static final String TX_MANAGER_FIELD_PREFIX = "txManager";

    @Override
    public void onBeforeParseService(ServiceElement service) {
        super.onBeforeParseService(service);
        TxModulatorContext context = new TxModulatorContext();
        service.setProperty(context);
    }

    protected TxModulatorContext modulatorContext() {
        return service().property(TxModulatorContext.class);
    }

    protected String propogationMethodName(TransactionPropagation propogation) {
        switch (propogation) {
            case REQUIRED:
                return TransactionManager.REQUIRED_METHOD;
            case REQUIRES_NEW:
                return TransactionManager.REQUIRES_NEW_METHOD;
            case MANDATORY:
                return TransactionManager.MANDATORY_METHOD;
            case NOT_SUPPORTED:
                return TransactionManager.NOT_SUPPORTED_METHOD;
            case SUPPORTS:
                return TransactionManager.SUPPORTS_METHOD;
            case NEVER:
                return TransactionManager.NEVER_METHOD;
            case NESTED:
                return TransactionManager.NESTED_METHOD;
            default:
                throw CodegenException.of().message("Unsupported transaction propogateion:" + propogation.name()).build();
        }
    }

    @Override
    public void onServiceMethodParsed(ServiceMethodElement proxyMethod) {
        super.onServiceMethodParsed(proxyMethod);

        AnnotationAssist<Transactional> txAnnotation = proxyMethod.originMethod().annotation(Transactional.class);
        if (txAnnotation == null) {
            txAnnotation = proxyMethod.parentService().originClass().annotation(Transactional.class);
            if (txAnnotation == null) {
                return;
            }
        }

        // Add manager field

        TxModulatorContext ctx = modulatorContext();
        Integer exIdx = ctx.txManagerIndex(txAnnotation.unwrap().manager());
        String managerFieldName = TX_MANAGER_FIELD_PREFIX + exIdx;

        FieldSpec txManagerFs = FieldSpec.builder(ClassName.get(TransactionManager.class), managerFieldName).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();
        ServiceFieldElement txManagerFe = new ServiceFieldElement(txManagerFs).inject();
        if (!StringUtils.isBlank(txAnnotation.unwrap().manager())) {
            txManagerFe.setNamed(txAnnotation.unwrap().manager());
        }
        proxyMethod.parentService().addCustomField(txManagerFe);

        // Add interceptor

        String propogationMethodName = propogationMethodName(txAnnotation.unwrap().propagation());

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("($N, $N)->", Interceptor.INVOCATION_CONTEXT_PARAM, Interceptor.OPTIONS_PARAM);
        //cb.add("$N.$N(()->$N.$N(),null)",
        cb.add("$N.$N($N::$N,null)",
                managerFieldName,
                propogationMethodName,
                Interceptor.INVOCATION_CONTEXT_PARAM,
                InvocationContext.PROCEED_METHOD
        );

        InterceptionElement interception = new InterceptionElement(cb.build());
        proxyMethod.addInterception(InterceptionPhases.TRANSACTION, interception);
    }
}
