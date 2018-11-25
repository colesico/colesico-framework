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
import colesico.framework.service.ServicePrototype;
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
import javax.tools.FileObject;
import javax.tools.StandardLocation;
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

    private void generateProducerAnnotation(TypeSpec.Builder producerBuilder) {
        AnnotationSpec.Builder b = AnnotationSpec.builder(Producer.class);
        b.addMember("value", "$S", Rank.RANK_MINOR);
        producerBuilder.addAnnotation(b.build());
    }

    private void generateProduceService(TypeSpec.Builder producerBuilder, ServiceElement serviceElement, String methodUID) {

        TypeName serviceProxyTypeName = ClassName.bestGuess(
                ServicePrototype.getProxyClassName(serviceElement.getOriginClass().asType().toString()));

        // Add  @Produce annotation
        AnnotationSpec.Builder produceAnn = AnnotationSpec.builder(Produce.class);
        produceAnn.addMember("value", "$T.class", serviceProxyTypeName);
        producerBuilder.addAnnotation(produceAnn.build());

        // Add produce method
        MethodSpec.Builder mb = MethodSpec.methodBuilder("get" + serviceElement.getOriginClass().getSimpleName() + methodUID);
        mb.addModifiers(Modifier.PUBLIC);
        mb.returns(TypeName.get(serviceElement.getOriginClass().asType()));

        String scopeAnnClassName = serviceElement.getScopeAnnotation() != null ? serviceElement.getScopeAnnotation().toString() : Singleton.class.getName();
        AnnotationSpec.Builder scopeAnnBuilder = AnnotationSpec.builder(ClassName.bestGuess(scopeAnnClassName));
        mb.addAnnotation(scopeAnnBuilder.build());

        mb.addParameter(serviceProxyTypeName, IMPLEMENTATION_PARAM);

        mb.addStatement("return $N", IMPLEMENTATION_PARAM);
        producerBuilder.addMethod(mb.build());
    }

    private void generateProduceTeleFacade(TypeSpec.Builder producerBuilder, TeleFacadeElement teleFacadeElement, String methodUID) {

        // Generate produce annotation
        AnnotationSpec.Builder produceAnn = AnnotationSpec.builder(Produce.class);
        produceAnn.addMember("value", "$T.class", ClassName.bestGuess(teleFacadeElement.getClassName()));
        producerBuilder.addAnnotation(produceAnn.build());


        // Generate produce method
        MethodSpec.Builder mb = MethodSpec.methodBuilder("get" + TeleFacade.class.getSimpleName() + methodUID);
        mb.addModifiers(Modifier.PUBLIC);
        mb.returns(TypeName.get(TeleFacade.class));
        mb.addParameter(ClassName.bestGuess(teleFacadeElement.getClassName()), IMPLEMENTATION_PARAM, Modifier.FINAL);

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

        producerBuilder.addMethod(mb.build());
    }

    private void generateProducerClass(String packageName, String classSimpleName, Set<ServiceElement> serviceElements) {
        String producerClassName = packageName + '.' + classSimpleName;
        logger.debug("Generate services internal producer: " + producerClassName);
        String producerPath = packageName.replace('.', '/') + "/" + classSimpleName + ".java";

        try {
            FileObject producerFile = context.getFiler().getResource(StandardLocation.SOURCE_OUTPUT, packageName, classSimpleName + ".java");
            producerFile.openInputStream();
            logger.info("Services IOC producer file exists: " + producerPath + ". Producer file will not be generated");
            context.getMessager().printMessage(Diagnostic.Kind.NOTE, "Services IOC producers class already exists: " + producerClassName + ". Clean the project to rebuild this class.");
            return;
        } catch (Exception e) {
            logger.info("IOC producer file is not exists, will be created: " + producerPath);
        }

        TypeSpec.Builder producerBuilder = TypeSpec.classBuilder(classSimpleName);
        producerBuilder.addModifiers(Modifier.PUBLIC);
        producerBuilder.addAnnotation(CodegenUtils.buildGenstampAnnotation(this.getClass().getName(), null, null));

        generateProducerAnnotation(producerBuilder);

        int i = 0;
        Element[] linkedElements = new Element[serviceElements.size()];
        for (ServiceElement service : serviceElements) {
            logger.debug("Generate service producing: " + service.getOriginClass().asType().toString());
            linkedElements[i] = service.getOriginClass();
            generateProduceService(producerBuilder, service, "F" + Integer.toString(i));

            // Telefacades producing
            for (TeleFacadeElement teleFacade : service.getTeleFacades()) {
                logger.debug("Generate tele-facade producing: " + teleFacade.getClassName());
                generateProduceTeleFacade(producerBuilder, teleFacade, "T" + Integer.toString(i));
            }

            i++;
        }

        final TypeSpec typeSpec = producerBuilder.build();
        CodegenUtils.createJavaFile(context.getProcessingEnv(), typeSpec, packageName, linkedElements);
    }

    public void generatePerPackage(Set<ServiceElement> services) {
        logger.debug("Generate services internal producers...");
        Map<String, Set<ServiceElement>> perPackageMap = new HashMap<>();
        for (ServiceElement srv : services) {
            String pkgName = CodegenUtils.getPackageName(srv.getOriginClass());
            Set<ServiceElement> pkgServices = perPackageMap.computeIfAbsent(pkgName, k -> new HashSet<>());
            pkgServices.add(srv);
        }

        for (Map.Entry<String, Set<ServiceElement>> e : perPackageMap.entrySet()) {
            String packageName = e.getKey();
            generateProducerClass(packageName, IOC_PRODUCER_CLASS_NAME, e.getValue());
        }
    }

    public void generatePerService(Set<ServiceElement> services) {
        logger.debug("Generate services internal producers...");
        for (ServiceElement srv : services) {
            String pkgName = CodegenUtils.getPackageName(srv.getOriginClass());
            String producerName = srv.getOriginClass().getSimpleName().toString() + "Producer";
            Set<ServiceElement> srvSet = new HashSet<>();
            srvSet.add(srv);
            generateProducerClass(pkgName, producerName, srvSet);
        }
    }

}
