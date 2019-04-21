/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.service.codegen.generator;


import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.ioc.*;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.ServiceProxy;
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
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author Vladlen Larionov
 */
public class IocGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    public static final String IOC_PRODUCER_CLASS_NAME = "ServicesProducer";
    public static final String IMPLEMENTATION_PARAM = "impl";

    protected final ProcessorContext context;

    public IocGenerator(ProcessorContext context) {
        this.context = context;
    }

    private void generateProduceService(ProducerGenerator producerGenerator, ServiceElement serviceElement, String methodUID) {
        TypeName serviceProxyTypeName = ClassName.bestGuess(serviceElement.getProxyClassName());

        // Add  @Produce annotation
        producerGenerator.addProduceAnnotation(serviceProxyTypeName);

        // Add produce method
        MethodSpec.Builder mb = producerGenerator.addProduceMethod("get" + serviceElement.getOriginClass().getSimpleName() + methodUID,
                TypeName.get(serviceElement.getOriginClass().asType()));


        String scopeAnnClassName = serviceElement.getCustomScopeType() == null ? Singleton.class.getName()
                : serviceElement.getCustomScopeType().asClassElement().getName();
        AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.bestGuess(scopeAnnClassName));
        mb.addAnnotation(scopeAnnBuilder.build());

        mb.addParameter(serviceProxyTypeName, IMPLEMENTATION_PARAM);

        mb.addStatement("return $N", IMPLEMENTATION_PARAM);
    }

    private void generateProduceTeleFacade(ProducerGenerator producerGenerator, TeleFacadeElement teleFacadeElement, String methodUID) {

        // Generate @Produce annotation
        producerGenerator.addProduceAnnotation(ClassName.bestGuess(teleFacadeElement.getFacadeClassName()));

        // Generate produce method
        MethodSpec.Builder mb = producerGenerator.addProduceMethod("get" + TeleFacade.class.getSimpleName() + methodUID,
                TypeName.get(TeleFacade.class));
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

    private void generateProducerClass(String packageName, String classSimpleName, Set<ServiceElement> serviceElements) {
        String producerClassName = packageName + '.' + classSimpleName;
        if (logger.isDebugEnabled()) {
            StringBuilder mb = new StringBuilder("Generate services producer: " + producerClassName).append("\n");
            for (ServiceElement se : serviceElements) {
                mb.append("  for service: ").append(se.getOriginClass().getName());
            }
            logger.debug(mb.toString());
        }


        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, classSimpleName, this.getClass(), context.getProcessingEnv());
        producerGenerator.setProducerRank(Rank.RANK_MINOR);

        if (producerGenerator.isProducerExists()) {
            if (context.isProductionCodegen()) {
                String producerPath = producerGenerator.getProducerClassFilePath();
                context.getMessager().printMessage(Diagnostic.Kind.WARNING, "Services IOC producers class already exists: " + producerPath + ". Clean the project to rebuild this class correctly.");
            }
        }

        int i = 0;
        Element[] linkedElements = new Element[serviceElements.size()];
        for (ServiceElement service : serviceElements) {
            logger.debug("Generate service producing: " + service.getOriginClass().asType().toString());
            linkedElements[i] = service.getOriginClass().unwrap();
            generateProduceService(producerGenerator, service, "F" + Integer.toString(i));

            // Telefacades producing
            for (TeleFacadeElement teleFacade : service.getTeleFacades()) {
                logger.debug("Generate tele-facade producing: " + teleFacade.getFacadeClassName());
                generateProduceTeleFacade(producerGenerator, teleFacade, "T" + Integer.toString(i));
            }

            i++;
        }

        context.getModulatorKit().notifyGenerateIocProducer(producerGenerator, serviceElements);

        final TypeSpec typeSpec = producerGenerator.typeBuilder().build();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, linkedElements);
    }

    public void generatePerPackage(Set<ServiceElement> services) {
        logger.debug("Generate services producers. One producer per package. Total services: " + services.size());
        Map<String, Set<ServiceElement>> perPackageMap = new HashMap<>();
        for (ServiceElement srv : services) {
            String pkgName = srv.getOriginClass().getPackageName();
            Set<ServiceElement> pkgServices = perPackageMap.computeIfAbsent(pkgName, k -> new HashSet<>());
            pkgServices.add(srv);
        }

        for (Map.Entry<String, Set<ServiceElement>> e : perPackageMap.entrySet()) {
            String packageName = e.getKey();
            generateProducerClass(packageName, IOC_PRODUCER_CLASS_NAME, e.getValue());
        }
    }

    public void generatePerService(Set<ServiceElement> services) {
        logger.debug("Generate services producers. One producer per service. Total services: " + services.size());
        for (ServiceElement srv : services) {
            String pkgName = srv.getOriginClass().getPackageName();
            String producerName = srv.getOriginClass().getSimpleName().toString() + "Producer";
            Set<ServiceElement> srvSet = new HashSet<>();
            srvSet.add(srv);
            generateProducerClass(pkgName, producerName, srvSet);
        }
    }

}
