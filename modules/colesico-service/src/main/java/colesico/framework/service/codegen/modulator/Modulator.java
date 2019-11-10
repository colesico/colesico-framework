/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.service.codegen.modulator;


import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ProcessorContext;
import colesico.framework.service.codegen.parser.RoundContext;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Deque;
import java.util.Set;

/**
 * Service extra code generator
 *
 * @author Vladlen Larionov
 */
abstract public class Modulator {

    protected ProcessorContext processorContext;
    protected RoundContext roundContext;

    protected ServiceElement service;
    protected ProxyMethodElement proxyMethod;

    /**
     * Returns a set of annotation that are aliases for @Service
     *
     * @return
     */
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return null;
    }

    public void onInit(ProcessorContext context) {
        this.processorContext = context;
    }

    public void onRoundStart(RoundContext context) {
        this.roundContext = context;
    }

    public void onRoundStop() {

    }

    public void onService(ServiceElement service) {
        this.service = service;
    }

    public void onProxyMethod(ProxyMethodElement proxyMethod) {
        this.proxyMethod = proxyMethod;
    }

    public void onAddTeleFacade(ServiceElement service) {
    }

    public void onAddTeleMethod(TeleMethodElement teleMethod) {

    }

    public void onLinkTeleParam(TeleParamElement teleParam, Deque<VarElement> varStack) {

    }

    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
    }

    public void onServiceParsed(ServiceElement service) {
    }

    public void onServiceGenerated(ServiceElement service) {

    }

    public void onGenerateIocProducer(ProducerGenerator generator, Set<ServiceElement> services) {

    }

    public final ProcessorContext getProcessorContext() {
        return processorContext;
    }

    public final RoundContext getRoundContext() {
        return roundContext;
    }

    public ServiceElement getService() {
        return service;
    }

    public ProxyMethodElement getProxyMethod() {
        return proxyMethod;
    }
}
