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

package colesico.framework.config.codegen;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.ioc.*;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class IocGenerator {

    public static final String FACTORY_PARAM = "factory";
    public static final String CONF_PARAM = "config";

    private Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    protected final ProcessingEnvironment processingEnv;
    protected final Elements elementUtils;
    protected final Types typeUtils;
    protected final Messager messager;
    protected final Filer filer;

    protected int producerIndex = 0;

    public IocGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    // Generates the configuration implementation producing  via annotation @Produce
    protected void generateProduceConfigurations(TypeSpec.Builder producerBuilder, ConfigElement confElement) {
        // Add  @Produce annotation
        AnnotationSpec.Builder produceAnn = AnnotationSpec.builder(Produce.class);
        produceAnn.addMember("value", "$T.class", TypeName.get(confElement.getImplementation().asType()));
        producerBuilder.addAnnotation(produceAnn.build());
    }


    //Generates config prototype producing methods based on configuration implementation, etc.
    protected void generateProduceExtraMethods(TypeSpec.Builder producerBuilder, ConfigElement confElement) {
        switch (confElement.getModel()) {
            case SINGLE: {
                generateProduceSingleConfigPrototype(producerBuilder, confElement);
                break;
            }
            case POLYVARIANT: {
                generateProducePolyvariantConfigPrototype(producerBuilder, confElement);
                break;
            }
            case MESSAGE: {
                generateProduceClassifiedConfigurable(producerBuilder, confElement);
                break;
            }
        }
    }

    private void generateProduceClassifiedConfigurable(TypeSpec.Builder producerBuilder, ConfigElement confElement) {
        TypeElement targetElm = confElement.getTarget().unwrap();
        MethodSpec.Builder pmb = MethodSpec.methodBuilder("get" + targetElm.getSimpleName().toString() + "With" + confElement.getImplementation().getSimpleName());
        pmb.addModifiers(Modifier.PUBLIC);
        pmb.returns(ClassName.bestGuess(targetElm.getQualifiedName().toString()));

        if (!confElement.getDefaultMessage()) {
            AnnotationSpec.Builder classedAnn = AnnotationSpec.builder(Classed.class);
            classedAnn.addMember("value", "$T.class", TypeName.get(confElement.getImplementation().asType()));
            pmb.addAnnotation(classedAnn.build());
        }

        pmb.addAnnotation(Singleton.class);

        //Parameter @Classed(AConfig.class) Supplier<Service> target
        ParameterSpec.Builder targetBuilder = ParameterSpec.builder(
                ParameterizedTypeName.get(ClassName.get(Supplier.class), TypeName.get(confElement.getTarget().asType())),
                FACTORY_PARAM,
                Modifier.FINAL
        );
        AnnotationSpec.Builder targetAnnBuilder = AnnotationSpec.builder(Classed.class);
        targetAnnBuilder.addMember("value", "$T.class", TypeName.get(confElement.getPrototype().asType()));
        targetBuilder.addAnnotation(targetAnnBuilder.build());
        pmb.addParameter(targetBuilder.build());

        //Parameter ConfImpl config
        ParameterSpec.Builder configBuilder = ParameterSpec.builder(
                TypeName.get(confElement.getImplementation().asType()),
                CONF_PARAM,
                Modifier.FINAL
        );
        pmb.addParameter(configBuilder.build());

        pmb.addStatement("return $N.$N($N)",
                FACTORY_PARAM,
                Supplier.GET_METHOD,
                CONF_PARAM
        );

        producerBuilder.addMethod(pmb.build());
    }

    protected void generateProducePolyvariantConfigPrototype(TypeSpec.Builder producerBuilder, ConfigElement confElement) {
        MethodSpec.Builder pmb = createProducingMethodBuilder(confElement, null);
        AnnotationSpec.Builder singletonAnn = AnnotationSpec.builder(Singleton.class);
        pmb.addAnnotation(singletonAnn.build());
        AnnotationSpec.Builder polyAnn = AnnotationSpec.builder(Polyproduce.class);
        pmb.addAnnotation(polyAnn.build());

        if (confElement.getClassedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Classed.class);
            ann.addMember("value", "$T", TypeName.get(confElement.getClassedQualifier()));
            pmb.addAnnotation(ann.build());
        } else if (confElement.getNamedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Named.class);
            ann.addMember("value", "$S", confElement.getNamedQualifier());
            pmb.addAnnotation(ann.build());
        }

        producerBuilder.addMethod(pmb.build());
    }

    protected void generateProduceSingleConfigPrototype(TypeSpec.Builder producerBuilder, ConfigElement confElement) {
        MethodSpec.Builder pmb = createProducingMethodBuilder(confElement, null);
        AnnotationSpec.Builder singletonAnn = AnnotationSpec.builder(Singleton.class);
        pmb.addAnnotation(singletonAnn.build());

        if (confElement.getClassedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Classed.class);
            ann.addMember("value", "$T", TypeName.get(confElement.getClassedQualifier()));
            pmb.addAnnotation(ann.build());
        } else if (confElement.getNamedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Named.class);
            ann.addMember("value", "$S", confElement.getNamedQualifier());
            pmb.addAnnotation(ann.build());
        }

        producerBuilder.addMethod(pmb.build());
    }

    protected MethodSpec.Builder createProducingMethodBuilder(final ConfigElement confElement, String methodSuffix) {
        if (methodSuffix == null) {
            methodSuffix = "";
        }
        String methodName = "get" + confElement.getImplementation().getSimpleName() + methodSuffix;
        MethodSpec.Builder mb = MethodSpec.methodBuilder(methodName);

        mb.addModifiers(Modifier.PUBLIC);
        mb.returns(TypeName.get(confElement.getPrototype().asType()));

        mb.addParameter(TypeName.get(confElement.getImplementation().asType()), CONF_PARAM, Modifier.FINAL);

        // Add return config internal
        mb.addStatement("return $N", CONF_PARAM);
        return mb;
    }

    protected String toProducerClassSimpleName(String rank) {
        String normalized = Normalizer.normalize(rank, Normalizer.Form.NFD);
        String rankToName = normalized.replaceAll("[^A-Za-z0-9]", "");
        rankToName = StrUtils.firstCharToUpperCase(rankToName);
        String indStr = "";
        if (producerIndex > 0) {
            indStr = Integer.toString(producerIndex);
        }
        String simpleClassName = "Config" + rankToName + indStr + "Producer";
        producerIndex++;
        return simpleClassName;
    }

    protected void generateProducerClass(String packageName, String rank, List<ConfigElement> configurationElements) {
        String producerClassSimpleName = toProducerClassSimpleName(rank);
        String producerPath = "/" + StringUtils.replace(packageName, ".", "/") + "/" + producerClassSimpleName + ".java";
        logger.debug("Generating configuration producer: " + producerPath);
        String producerClassName = packageName + '.' + producerClassSimpleName;

        try {
            FileObject producerFile = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, producerClassSimpleName + ".java");
            producerFile.openInputStream();
            logger.info("Configurations IOC producer file exists: " + producerPath + ". Producer file will not be generated");
            messager.printMessage(Diagnostic.Kind.NOTE, "Configurations IOC producers class already exists: " + producerClassName + ". Clean the project to rebuild this class.");
            return;
        } catch (Exception e) {
            logger.info("IOC producer file is not exists, will be created: " + producerPath);
        }

        TypeSpec.Builder producerBuilder = TypeSpec.classBuilder(producerClassSimpleName);
        producerBuilder.addModifiers(Modifier.PUBLIC);
        producerBuilder.addAnnotation(CodegenUtils.generateGenstamp(this.getClass().getName(), null, null));

        AnnotationSpec.Builder b = AnnotationSpec.builder(Producer.class);
        b.addMember("value", "$S", rank);
        producerBuilder.addAnnotation(b.build());

        List<TypeElement> typeElements = new ArrayList<>();
        for (ConfigElement celm : configurationElements) {
            logger.debug("Generate config producing for: " + celm.toString());
            typeElements.add(celm.getImplementation().unwrap());
            generateProduceConfigurations(producerBuilder, celm);
            generateProduceExtraMethods(producerBuilder, celm);
        }

        final TypeSpec typeSpec = producerBuilder.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, packageName, typeElements.toArray(new TypeElement[typeElements.size()]));
    }

}
