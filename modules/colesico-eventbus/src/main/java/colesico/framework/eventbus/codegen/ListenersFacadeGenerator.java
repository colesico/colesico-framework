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

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.eventbus.EventBinding;
import colesico.framework.eventbus.EventsListener;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import com.palantir.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import java.util.List;

public class ListenersFacadeGenerator {

    public static final String FACADE_SUFFIX = "Listener";
    public static final String EVENT_PARAM = "event";
    public static final String TARGET_VAR = "target";

    private final Logger logger = LoggerFactory.getLogger(ListenersFacadeGenerator.class);
    private final ServiceProcessorContext context;

    public ListenersFacadeGenerator(ServiceProcessorContext context) {
        this.context = context;
    }

    protected void generateConstructor(ServiceElement service, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(service.getOriginClass().getOriginType())),
                EventsListener.TARGET_PROV_FIELD,
                Modifier.FINAL);

        mb.addStatement("super($N)", EventsListener.TARGET_PROV_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected void generateHandlerProxy(ServiceElement service, EventHandlerElement handler, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(handler.getOriginMethod().getName());
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        ParameterSpec.Builder pb = ParameterSpec.builder(TypeName.get(handler.getEventType().unwrap()), EVENT_PARAM, Modifier.FINAL);
        mb.addParameter(pb.build());

        mb.addStatement("$T $N=this.$N.get()",
                TypeName.get(service.getOriginClass().getOriginType()),
                TARGET_VAR,
                EventsListener.TARGET_PROV_FIELD);
        mb.addStatement("$N.$N($N)",
                TARGET_VAR,
                handler.getOriginMethod().getName(),
                EVENT_PARAM
        );

        classBuilder.addMethod(mb.build());
    }

    protected void generateHandlersProxies(ServiceElement service, List<EventHandlerElement> handlers, TypeSpec.Builder classBuilder) {
        for (EventHandlerElement handler : handlers) {
            generateHandlerProxy(service, handler, classBuilder);
        }
    }

    protected void generateGetBindingsMethod(ServiceElement service, List<EventHandlerElement> handlers, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(EventsListener.GET_BINDINGS_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        TypeName resType = ArrayTypeName.of(ClassName.get(EventBinding.class));
        mb.returns(resType);

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("return new $T{\n", resType);
        cb.indent();

        int i = 0;
        for (EventHandlerElement ele : handlers) {
            i++;
            cb.add("new $T<>($T.class, this::$L)",
                    ClassName.get(EventBinding.class),
                    TypeName.get(ele.getEventType().unwrap()),
                    ele.getOriginMethod().getName()
            );
            if (i < handlers.size()) {
                cb.add(",\n");
            } else {
                cb.add("\n");
            }
        }

        cb.unindent();
        cb.add("};\n");

        mb.addCode(cb.build());

        classBuilder.addMethod(mb.build());
    }

    public String getListenersFacadeClassName(ServiceElement service) {
        String facadeClassSimpleName = service.getOriginClass().getSimpleName() + FACADE_SUFFIX;
        return facadeClassSimpleName;
    }

    public void generateListenersFacade(ServiceElement service, List<EventHandlerElement> handlers) {
        String facadeClassSimpleName = getListenersFacadeClassName(service);
        logger.debug("Generate Events listener facade '"+facadeClassSimpleName+"' for service: " + service.getOriginClass().getName());

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(facadeClassSimpleName);
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(EventsListener.class), TypeName.get(service.getOriginClass().getOriginType())));

        classBuilder.addAnnotation(CodegenUtils.generateGenstamp(this.getClass().getName(), null, null));
        classBuilder.addAnnotation(Singleton.class);

        generateConstructor(service, classBuilder);
        generateHandlersProxies(service, handlers, classBuilder);
        generateGetBindingsMethod(service, handlers, classBuilder);

        final TypeSpec typeSpec = classBuilder.build();
        String packageName = service.getOriginClass().getPackageName();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, service.getOriginClass().unwrap());
    }
}
