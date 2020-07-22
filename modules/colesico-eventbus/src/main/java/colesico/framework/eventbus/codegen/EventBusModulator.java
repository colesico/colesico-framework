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

package colesico.framework.eventbus.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.eventbus.EventsListener;
import colesico.framework.eventbus.OnEvent;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.service.codegen.model.ProxyMethodElement;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.service.codegen.parser.ProcessorContext;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class EventBusModulator extends Modulator {

    private ListenersFacadeGenerator facadeGenerator;

    @Override
    public void onInit(ProcessorContext context) {
        super.onInit(context);
        facadeGenerator = new ListenersFacadeGenerator(context);
    }

    @Override
    public void onService(ServiceElement service) {
        super.onService(service);
    }

    @Override
    public void onProxyMethod(ProxyMethodElement proxyMethod) {
        super.onProxyMethod(proxyMethod);

        AnnotationAssist<OnEvent> handlerAnn = proxyMethod.getOriginMethod().getAnnotation(OnEvent.class);
        if (handlerAnn == null) {
            return;
        }

        List<ParameterElement> params = proxyMethod.getOriginMethod().getParameters();

        if (params.size() != 1) {
            throw CodegenException.of().
                    message("Event handler must have only one argument")
                    .element(proxyMethod.getOriginMethod())
                    .build();
        }

        ClassType eventType = params.get(0).asClassType();
        if (eventType == null) {
            throw CodegenException.of().message("Unsupported event type kind").element(params.get(0).unwrap()).build();
        }
        EventHandlerElement evlElement = new EventHandlerElement(proxyMethod.getOriginMethod(), eventType);
        proxyMethod.setProperty(evlElement);
    }

    @Override
    public void onServiceGenerated(ServiceElement service) {
        super.onServiceGenerated(service);

        List<EventHandlerElement> handlers = getEventHandlers(service);
        if (!handlers.isEmpty()) {
            facadeGenerator.generateListenersFacade(service, handlers);
        }

    }


    @Override
    public void onGenerateIocProducer(ProducerGenerator generator, Set<ServiceElement> services) {
        super.onGenerateIocProducer(generator, services);

        for (ServiceElement service : services) {

            List<EventHandlerElement> handlers = getEventHandlers(service);
            if (handlers.isEmpty()) {
                continue;
            }

            String listnerFacadeClassName = service.getOriginClass().getPackageName() + '.' +
                    facadeGenerator.getListenersFacadeClassName(service);

            TypeName facadeType = ClassName.bestGuess(listnerFacadeClassName);
            generator.addProduceAnnotation(facadeType);

            String methodName = "get" + facadeGenerator.getListenersFacadeClassName(service);
            MethodSpec.Builder mb = generator.addProduceMethod(methodName, ClassName.get(EventsListener.class));
            mb.addAnnotation(Polyproduce.class);
            mb.addParameter(facadeType, "impl", Modifier.FINAL);
            mb.addStatement("return impl");
        }
    }

    protected List<EventHandlerElement> getEventHandlers(ServiceElement service) {
        List<EventHandlerElement> handlers = new ArrayList<>();
        for (ProxyMethodElement pm : service.getProxyMethods()) {
            EventHandlerElement el = pm.getProperty(EventHandlerElement.class);
            if (el != null) {
                handlers.add(el);
            }
        }
        return handlers;
    }
}
