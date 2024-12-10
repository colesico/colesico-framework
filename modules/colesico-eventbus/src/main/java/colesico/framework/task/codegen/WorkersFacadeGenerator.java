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

import colesico.framework.task.registry.ServiceWorkers;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.task.registry.TaskBinding;
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

public class WorkersFacadeGenerator {

    public static final String FACADE_SUFFIX = "Worker";
    public static final String TASK_PARAM = "task";
    public static final String TARGET_VAR = "target";

    private final Logger logger = LoggerFactory.getLogger(WorkersFacadeGenerator.class);
    private final ServiceProcessorContext context;

    public WorkersFacadeGenerator(ServiceProcessorContext context) {
        this.context = context;
    }

    protected void generateConstructor(ServiceElement service, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(service.getOriginClass().getOriginType())),
                ServiceWorkers.SERVICE_PROV_FIELD,
                Modifier.FINAL);

        mb.addStatement("super($N)", ServiceWorkers.SERVICE_PROV_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected void generateHandlerProxy(ServiceElement service, TaskWorkerElement handler, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(handler.getOriginMethod().getName());
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        ParameterSpec.Builder pb = ParameterSpec.builder(TypeName.get(handler.getTaskType().unwrap()), TASK_PARAM, Modifier.FINAL);
        mb.addParameter(pb.build());

        mb.addStatement("$T $N=this.$N.get()",
                TypeName.get(service.getOriginClass().getOriginType()),
                TARGET_VAR,
                ServiceWorkers.SERVICE_PROV_FIELD);
        mb.addStatement("$N.$N($N)",
                TARGET_VAR,
                handler.getOriginMethod().getName(),
                TASK_PARAM
        );

        classBuilder.addMethod(mb.build());
    }

    protected void generateHandlersProxies(ServiceElement service, List<TaskWorkerElement> handlers, TypeSpec.Builder classBuilder) {
        for (TaskWorkerElement handler : handlers) {
            generateHandlerProxy(service, handler, classBuilder);
        }
    }

    protected void generateGetBindingsMethod(ServiceElement service, List<TaskWorkerElement> handlers, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(ServiceWorkers.GET_BINDINGS_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        TypeName resType = ArrayTypeName.of(ClassName.get(TaskBinding.class));
        mb.returns(resType);

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("return new $T{\n", resType);
        cb.indent();

        int i = 0;
        for (TaskWorkerElement ele : handlers) {
            i++;
            cb.add("new $T<>($T.class, this::$L)",
                    ClassName.get(TaskBinding.class),
                    TypeName.get(ele.getTaskType().unwrap()),
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

    public String getWorkersFacadeClassName(ServiceElement service) {
        String facadeClassSimpleName = service.getOriginClass().getSimpleName() + FACADE_SUFFIX;
        return facadeClassSimpleName;
    }

    public void generateWorkersFacade(ServiceElement service, List<TaskWorkerElement> handlers) {
        String facadeClassSimpleName = getWorkersFacadeClassName(service);
        logger.debug("Generate Tasks worker facade '"+facadeClassSimpleName+"' for service: " + service.getOriginClass().getName());

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(facadeClassSimpleName);
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.superclass(ParameterizedTypeName.get(ClassName.get(ServiceWorkers.class), TypeName.get(service.getOriginClass().getOriginType())));

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
