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
package colesico.framework.service.codegen.modulator;


import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.service.codegen.parser.RoundContext;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Service extra parser/code generator
 */
abstract public class Modulator {

    protected ServiceProcessorContext processorContext;
    protected RoundContext roundContext;

    protected ServiceElement service;
    protected ServiceMethodElement serviceMethod;

    /**
     * Returns a set of annotation that are aliases for @Service
     */
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return null;
    }

    /**
     * When this modulator should receive events before or after the
     * specified modulator or it doesn't matter
     */
    public ListenOrder listenOrder(Class<? extends Modulator> thatModulator) {
        return ListenOrder.NO_MATTER;
    }

    public void onInit(ServiceProcessorContext context) {
        this.processorContext = context;
    }

    public void onRoundStart(RoundContext context) {
        this.roundContext = context;
    }

    public void onRoundStop() {

    }

    public void onBeforeParseService(ServiceElement service) {
        this.service = service;
    }

    public void onServiceMethodParsed(ServiceMethodElement serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public void onInitTeleFacade(ServiceElement service) {
    }

    public void onBeforeParseTeleFacade(TeleFacadeElement teleFacade) {
    }

    public void onBeforeParseTeleMethod(TeleMethodElement teleMethod) {

    }

    public void onTeleInputParsed(TeleInputElement teleInput) {

    }

    public void onTeleMethodParsed(TeleMethodElement teleMethod) {

    }

    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
    }

    public void onServiceParsed(ServiceElement service) {
    }

    public void onServiceGenerated(ServiceElement service) {

    }

    public void onGenerateIocProducer(ProducerGenerator generator, ServiceElement service) {

    }

    public final ServiceProcessorContext getProcessorContext() {
        return processorContext;
    }

    public final RoundContext getRoundContext() {
        return roundContext;
    }

    public ServiceElement getService() {
        return service;
    }

    public ServiceMethodElement getServiceMethod() {
        return serviceMethod;
    }

    public enum ListenOrder {
        BEFORE, AFTER, NO_MATTER;
    }
}
