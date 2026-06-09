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

package colesico.framework.service.codegen.generator;


import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleServiceElement;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.teleapi.TeleFacade;
import com.palantir.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import javax.lang.model.element.Modifier;

import static colesico.framework.assist.StringUtils.isBlank;

/**
 * Service and tele-facade producer generator
 */
public class IocGenerator extends FrameworkAbstractGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    public static final String IMPLEMENTATION_PARAM = "impl";

    protected final ServiceProcessorContext context;

    public IocGenerator(ServiceProcessorContext context) {
        super(context.processingEnv());
        this.context = context;
    }

    private void generateProduceService(ProducerGenerator producerGenerator, ServiceElement serviceElm) {

        TypeName serviceProxyTypeName = ClassName.bestGuess(serviceElm.proxyClassName());

        // Add  @Produce annotation
        producerGenerator.addProduceAnnotation(serviceProxyTypeName);

        // Add produce method
        String methodName = StringUtils.firstCharToLowerCase(serviceElm.originClass().simpleName());
        MethodSpec.Builder mb = producerGenerator.addProduceMethod(methodName, TypeName.get(serviceElm.originClass().originType()));

        if (serviceElm.customScopeType() == null) {
            AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.get(Singleton.class));
            mb.addAnnotation(scopeAnnBuilder.build());
        }

        mb.addParameter(serviceProxyTypeName, IMPLEMENTATION_PARAM);

        mb.addStatement("return $N", IMPLEMENTATION_PARAM);

    }

    private void generateProduceTeleFacade(ProducerGenerator producerGenerator, TeleServiceElement teleService) {

        // Generate @Produce annotation
        producerGenerator.addProduceAnnotation(ClassName.bestGuess(teleService.facadeClassName()));

        // Generate produce method
        String methodName = StringUtils.firstCharToLowerCase(teleService.facadeClassSimpleName());
        MethodSpec.Builder mb = producerGenerator.addProduceMethod(methodName, TypeName.get(TeleFacade.class));
        mb.addParameter(ClassName.bestGuess(teleService.facadeClassName()), IMPLEMENTATION_PARAM, Modifier.FINAL);

        mb.addAnnotation(Polyproduce.class);

        // Add IOC qualifiers
        if (!isBlank(teleService.iocQualifier().named())) {
            AnnotationSpec.Builder anb = AnnotationSpec.builder(Named.class);
            anb.addMember("value", "$S", teleService.iocQualifier().named());
            mb.addAnnotation(anb.build());
        } else if (teleService.iocQualifier().classed() != null) {
            AnnotationSpec.Builder anb = AnnotationSpec.builder(Classed.class);
            anb.addMember("value", "$T.class", ClassName.bestGuess(teleService.iocQualifier().classed()));
            mb.addAnnotation(anb.build());
        } else {
            // Default add classed as tele-type
            AnnotationSpec.Builder anb = AnnotationSpec.builder(Classed.class);
            anb.addMember("value", "$T.class", ClassName.get(teleService.teleType()));
            mb.addAnnotation(anb.build());
        }

        mb.addStatement("return $N", IMPLEMENTATION_PARAM);
    }

    private void generateProduceTeleInterceptor(ProducerGenerator producerGenerator, TeleServiceElement teleService) {
        producerGenerator.addProduceAnnotation(ClassName.bestGuess(teleService.interceptorClassName()));
    }

    public void generate(ServiceElement service) {

        String producerClassSimpleName = service.originClass().simpleName();
        String packageName = service.originClass().packageName();

        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), context.processingEnv());

        // Service producing
        generateProduceService(producerGenerator, service);

        // Tele-facade producing
        TeleServiceElement teleService = service.teleService();
        if (teleService != null) {
            logger.debug("Generate tele-facade producing: " + service.proxyClassName());
            generateProduceTeleFacade(producerGenerator, teleService);
            // Interceptor producing
            generateProduceTeleInterceptor(producerGenerator, teleService);
        }

        context.modulatorKit().notifyGenerateIocProducer(producerGenerator, service);

        final TypeSpec typeSpec = producerGenerator.typeBuilder().build();
        CodegenUtils.createJavaFile(context.processingEnv(), typeSpec, packageName, service.originClass().unwrap());
    }

}
