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

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.config.ConfigSourceDriver;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Supplier;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String FACTORY_PARAM = "factory";
    public static final String CONF_PARAM = "config";

    private Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected MethodSpec.Builder createProducingOnPrototypeMethodBuilder(final ProducerGenerator prodGen, final ConfigElement confElement) {
        String methodName = "get" + confElement.getImplementation().getSimpleName();
        MethodSpec.Builder mb = prodGen.addProduceMethod(methodName, TypeName.get(confElement.getPrototype().asType()));
        mb.addParameter(TypeName.get(confElement.getImplementation().asType()), CONF_PARAM, Modifier.FINAL);
        // Add return config internal
        mb.addStatement("return $N", CONF_PARAM);
        return mb;
    }

    protected void generateProduceSingleConfigPrototype(ProducerGenerator prodGen, ConfigElement confElement) {
        MethodSpec.Builder mb = createProducingOnPrototypeMethodBuilder(prodGen, confElement);
        AnnotationSpec.Builder singletonAnn = AnnotationSpec.builder(Singleton.class);
        mb.addAnnotation(singletonAnn.build());

        if (confElement.getClassedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Classed.class);
            ann.addMember("value", "$T", TypeName.get(confElement.getClassedQualifier()));
            mb.addAnnotation(ann.build());
        } else if (confElement.getNamedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Named.class);
            ann.addMember("value", "$S", confElement.getNamedQualifier());
            mb.addAnnotation(ann.build());
        }
    }

    private void generateProducePolyvariantConfigPrototype(ProducerGenerator prodGen, ConfigElement confElement) {
        MethodSpec.Builder mb = createProducingOnPrototypeMethodBuilder(prodGen, confElement);
        AnnotationSpec.Builder singletonAnn = AnnotationSpec.builder(Singleton.class);
        mb.addAnnotation(singletonAnn.build());
        AnnotationSpec.Builder polyAnn = AnnotationSpec.builder(Polyproduce.class);
        mb.addAnnotation(polyAnn.build());

        if (confElement.getClassedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Classed.class);
            ann.addMember("value", "$T", TypeName.get(confElement.getClassedQualifier()));
            mb.addAnnotation(ann.build());
        } else if (confElement.getNamedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Named.class);
            ann.addMember("value", "$S", confElement.getNamedQualifier());
            mb.addAnnotation(ann.build());
        }
    }

    private void generateProduceClassifiedConfigurable(ProducerGenerator prodGen, ConfigElement confElement) {
        TypeElement targetElm = confElement.getTarget().unwrap();
        MethodSpec.Builder mb = prodGen.addProduceMethod("get" + targetElm.getSimpleName().toString() + "With" + confElement.getImplementation().getSimpleName(),
            ClassName.bestGuess(targetElm.getQualifiedName().toString()));

        if (!confElement.getDefaultMessage()) {
            AnnotationSpec.Builder classedAnn = AnnotationSpec.builder(Classed.class);
            classedAnn.addMember("value", "$T.class", TypeName.get(confElement.getImplementation().asType()));
            mb.addAnnotation(classedAnn.build());
        }

        mb.addAnnotation(Singleton.class);

        //Parameter @Classed(AConfig.class) Supplier<Service> target
        ParameterSpec.Builder targetBuilder = ParameterSpec.builder(
            ParameterizedTypeName.get(ClassName.get(Supplier.class), TypeName.get(confElement.getTarget().asType())),
            FACTORY_PARAM,
            Modifier.FINAL
        );
        AnnotationSpec.Builder targetAnnBuilder = AnnotationSpec.builder(Classed.class);
        targetAnnBuilder.addMember("value", "$T.class", TypeName.get(confElement.getPrototype().asType()));
        targetBuilder.addAnnotation(targetAnnBuilder.build());
        mb.addParameter(targetBuilder.build());

        //Parameter ConfImpl config
        ParameterSpec.Builder configBuilder = ParameterSpec.builder(
            TypeName.get(confElement.getImplementation().asType()),
            CONF_PARAM,
            Modifier.FINAL
        );
        mb.addParameter(configBuilder.build());

        mb.addStatement("return $N.$N($N)",
            FACTORY_PARAM,
            Supplier.GET_METHOD,
            CONF_PARAM
        );
    }

    private void generateInitSourceValues(String postProduceMethodName, ConfigElement confElement, ProducerGenerator prodGen) {
        MethodSpec.Builder mb = prodGen.addMethod(postProduceMethodName, Modifier.PROTECTED);
        mb.addParameter(TypeName.get(confElement.getImplementation().asClassType().unwrap()), postProduceMethodName);
        mb.returns(TypeName.VOID);
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.addStatement("final $T connection = null",ClassName.get(ConfigSourceDriver.Connection.class));
        cb.add("try {\n");
        cb.indent();
        //TODO: init values code
        cb.unindent();
        cb.add("} finally {\n");
        cb.indent();
        cb.addStatement("connection.$N()", ConfigSourceDriver.Connection.CLOSE_METHD);
        cb.unindent();
        cb.add("}\n");
        mb.addCode(cb.build());
    }

    private void generateProducerClass(ConfigElement confElement) {
        String classSimpleName = confElement.getImplementation().getSimpleName();
        String packageName = confElement.getImplementation().getPackageName();

        ProducerGenerator prodGen = new ProducerGenerator(packageName, classSimpleName, this.getClass(), getProcessingEnv());
        prodGen.setProducerRank(confElement.getRank());

        // Generates the configuration implementation producing  via annotation @Produce
        AnnotationSpec.Builder produceAnn = prodGen.addProduceAnnotation(TypeName.get(confElement.getImplementation().asType()));

        if (confElement.getPrototype() != null) {
            //Generates config prototype producing methods based on configuration implementation, etc.
            switch (confElement.getModel()) {
                case SINGLE: {
                    generateProduceSingleConfigPrototype(prodGen, confElement);
                    break;
                }
                case POLYVARIANT: {
                    generateProducePolyvariantConfigPrototype(prodGen, confElement);
                    break;
                }
                case MESSAGE: {
                    generateProduceClassifiedConfigurable(prodGen, confElement);
                    break;
                }
            }
        }

        // Init configuration source values
        if (confElement.getSource() != null) {
            String postProduceMethodName = "init" + classSimpleName;
            produceAnn.addMember(Produce.POST_PRODUCE_METHOD, "$S", postProduceMethodName);
            generateInitSourceValues(postProduceMethodName, confElement, prodGen);
        }
        
        prodGen.generate(confElement.getImplementation().unwrap());
    }

    public void generate(List<ConfigElement> configElements) {
        for (ConfigElement confElement : configElements) {
            logger.debug("Generate config producing for: " + confElement.toString());
            generateProducerClass(confElement);
        }
    }
}
