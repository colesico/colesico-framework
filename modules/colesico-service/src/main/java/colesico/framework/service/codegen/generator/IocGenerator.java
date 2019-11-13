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

package colesico.framework.service.codegen.generator;


import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.parser.ProcessorContext;
import colesico.framework.teleapi.TeleFacade;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.*;


/**
 * @author Vladlen Larionov
 */
public class IocGenerator extends FrameworkAbstractGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    public static final String IMPLEMENTATION_PARAM = "impl";

    protected final ProcessorContext context;

    public IocGenerator(ProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
    }

    private void generateProduceService(ProducerGenerator producerGenerator, List<ServiceElement> srvElements) {
        for (ServiceElement srvElm : srvElements) {
            TypeName serviceProxyTypeName = ClassName.bestGuess(srvElm.getProxyClassName());

            // Add  @Produce annotation
            producerGenerator.addProduceAnnotation(serviceProxyTypeName);

            // Add produce method
            String methodName = "get" + srvElm.getOriginClass().getSimpleName();
            MethodSpec.Builder mb = producerGenerator.addProduceMethod(methodName, TypeName.get(srvElm.getOriginClass().asType()));

            if (srvElm.getCustomScopeType() == null) {
                AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.get(Singleton.class));
                mb.addAnnotation(scopeAnnBuilder.build());
            }

            mb.addParameter(serviceProxyTypeName, IMPLEMENTATION_PARAM);

            mb.addStatement("return $N", IMPLEMENTATION_PARAM);
        }
    }

    private void generateProduceTeleFacade(ProducerGenerator producerGenerator, TeleFacadeElement teleFacadeElement) {

        // Generate @Produce annotation
        producerGenerator.addProduceAnnotation(ClassName.bestGuess(teleFacadeElement.getFacadeClassName()));

        // Generate produce method
        String methodName = "get" + TeleFacade.class.getSimpleName() + StrUtils.firstCharToUpperCase(teleFacadeElement.getTeleType());
        MethodSpec.Builder mb = producerGenerator.addProduceMethod(methodName, TypeName.get(TeleFacade.class));
        mb.addParameter(ClassName.bestGuess(teleFacadeElement.getFacadeClassName()), IMPLEMENTATION_PARAM, Modifier.FINAL);

        mb.addAnnotation(Polyproduce.class);

        // Add IOC qualifiers
        if (StringUtils.isNotEmpty(teleFacadeElement.getIocQualifiers().getNamed())) {
            AnnotationSpec.Builder anb = AnnotationSpec.builder(Named.class);
            anb.addMember("value", "$S", teleFacadeElement.getIocQualifiers().getNamed());
            mb.addAnnotation(anb.build());
        } else if (teleFacadeElement.getIocQualifiers().getClassed() != null) {
            AnnotationSpec.Builder anb = AnnotationSpec.builder(Classed.class);
            anb.addMember("value", "$T.class", ClassName.bestGuess(teleFacadeElement.getIocQualifiers().getClassed()));
            mb.addAnnotation(anb.build());
        }

        mb.addStatement("return $N", IMPLEMENTATION_PARAM);
    }

    private void generateProducerClass(String producerClassSimpleName, String packageName, List<ServiceElement> srvElements) {

        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), context.getProcessingEnv());

        // Service producing
        generateProduceService(producerGenerator, srvElements);

        Element[] linkedElms = new Element[srvElements.size()];
        int idx = 0;
        for (ServiceElement srvElm : srvElements) {
            linkedElms[idx++] = srvElm.getOriginClass().unwrap();
            // Telefacades producing
            for (TeleFacadeElement teleFacade : srvElm.getTeleFacades()) {
                logger.debug("Generate tele-facade producing: " + teleFacade.getFacadeClassName());
                generateProduceTeleFacade(producerGenerator, teleFacade);
            }
        }
        context.getModulatorKit().notifyGenerateIocProducer(producerGenerator, new HashSet<>(srvElements));

        final TypeSpec typeSpec = producerGenerator.typeBuilder().build();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, linkedElms);
    }

    public void generate() {
        if (getCodegenMode().isDefault()) {
            // Generate IoC producer per service
            for (ServiceElement srvElm : context.getProcessedServices()) {
                logger.debug("Generate IoC producer for service: " + srvElm.getOriginClass().getName());
                String producerClassSimpleName = srvElm.getOriginClass().getSimpleName();
                String packageName = srvElm.getOriginClass().getPackageName();
                generateProducerClass(producerClassSimpleName, packageName, List.of(srvElm));
            }
        } else {
            // Group services per packages
            Map<String, List<ServiceElement>> byPackageMap = new HashMap<>();
            for (ServiceElement srvElm : context.getProcessedServices()) {
                String packageName = srvElm.getOriginClass().getPackageName();
                List<ServiceElement> pkgServices = byPackageMap.computeIfAbsent(packageName, k -> new ArrayList<>());
                pkgServices.add(srvElm);
            }

            // Generate single producer per package
            for (Map.Entry<String, List<ServiceElement>> entry : byPackageMap.entrySet()) {
                String packageName = entry.getKey();
                String producerClassSimpleName = "ServicesProducer";
                logger.debug("Generate IoC producer for services in package: " + packageName);
                generateProducerClass(producerClassSimpleName, packageName, entry.getValue());
            }
        }
    }
}
