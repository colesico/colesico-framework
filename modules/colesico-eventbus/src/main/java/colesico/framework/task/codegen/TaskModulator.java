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

package colesico.framework.task.codegen;

import colesico.framework.task.registry.ServiceListener;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.task.OnTask;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.service.codegen.model.ServiceMethodElement;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;


public class TaskModulator extends Modulator {

    private ListenersFacadeGenerator facadeGenerator;

    @Override
    public void onInit(ServiceProcessorContext context) {
        super.onInit(context);
        facadeGenerator = new ListenersFacadeGenerator(context);
    }

    @Override
    public void onBeforeParseService(ServiceElement service) {
        super.onBeforeParseService(service);
    }

    @Override
    public void onServiceMethodParsed(ServiceMethodElement proxyMethod) {
        super.onServiceMethodParsed(proxyMethod);

        AnnotationAssist<OnTask> handlerAnn = proxyMethod.getOriginMethod().getAnnotation(OnTask.class);
        if (handlerAnn == null) {
            return;
        }

        List<ParameterElement> params = proxyMethod.getOriginMethod().getParameters();

        if (params.size() != 1) {
            throw CodegenException.of().
                    message("Task handler must have only one argument")
                    .element(proxyMethod.getOriginMethod())
                    .build();
        }

        ClassType taskType = params.get(0).asClassType();
        if (taskType == null) {
            throw CodegenException.of().message("Unsupported task type kind").element(params.get(0).unwrap()).build();
        }
        TaskListenerElement evlElement = new TaskListenerElement(proxyMethod.getOriginMethod(), taskType);
        proxyMethod.setProperty(evlElement);
    }

    @Override
    public void onServiceGenerated(ServiceElement service) {
        super.onServiceGenerated(service);

        List<TaskListenerElement> handlers = getTaskHandlers(service);
        if (!handlers.isEmpty()) {
            facadeGenerator.generateListenersFacade(service, handlers);
        }

    }


    @Override
    public void onGenerateIocProducer(ProducerGenerator generator, ServiceElement service) {
        super.onGenerateIocProducer(generator, service);

        List<TaskListenerElement> handlers = getTaskHandlers(service);
        if (handlers.isEmpty()) {
            return;
        }

        String listnerFacadeClassName = service.getOriginClass().getPackageName() + '.' +
                facadeGenerator.getListenersFacadeClassName(service);

        TypeName facadeType = ClassName.bestGuess(listnerFacadeClassName);
        generator.addProduceAnnotation(facadeType);

        String methodName = "get" + facadeGenerator.getListenersFacadeClassName(service);
        MethodSpec.Builder mb = generator.addProduceMethod(methodName, ClassName.get(ServiceListener.class));
        mb.addAnnotation(Polyproduce.class);
        mb.addParameter(facadeType, "impl", Modifier.FINAL);
        mb.addStatement("return impl");
    }

    protected List<TaskListenerElement> getTaskHandlers(ServiceElement service) {
        List<TaskListenerElement> handlers = new ArrayList<>();
        for (ServiceMethodElement pm : service.getServiceMethods()) {
            TaskListenerElement el = pm.getProperty(TaskListenerElement.class);
            if (el != null) {
                handlers.add(el);
            }
        }
        return handlers;
    }
}
