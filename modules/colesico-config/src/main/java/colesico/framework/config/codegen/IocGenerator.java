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

package colesico.framework.config.codegen;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.ArrayCodegen;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.config.ConfigSource;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.listener.PostProduce;
import colesico.framework.ioc.message.Message;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Supplier;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String FACTORY_PARAM = "factory";
    public static final String CONF_PARAM = "config";
    public static final String CONFIG_SOURCE_PARAM = "configSource";
    public static final String CONNECTION_VAR = "conn";
    public static final String BAG_VAR = "configData";

    private Logger logger = LoggerFactory.getLogger(IocGenerator.class);

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected MethodSpec.Builder createProducingOnPrototypeMethodBuilder(final ProducerGenerator prodGen, final ConfigElement confElement) {
        String methodName = "get" + confElement.getImplementation().getSimpleName();
        MethodSpec.Builder mb = prodGen.addProduceMethod(methodName, TypeName.get(confElement.getPrototype().getOriginType()));
        // Config impl param
        ParameterSpec.Builder pb = ParameterSpec.builder(TypeName.get(confElement.getImplementation().getOriginType()), CONF_PARAM, Modifier.FINAL);
        if (confElement.getClassedQualifier() != null) {
            AnnotationSpec.Builder classedAnn = AnnotationSpec.builder(Classed.class);
            classedAnn.addMember("value", "$T.class", TypeName.get(confElement.getClassedQualifier()));
            pb.addAnnotation(classedAnn.build());
        } else if (confElement.getNamedQualifier() != null) {
            AnnotationSpec.Builder namedAnn = AnnotationSpec.builder(Named.class);
            namedAnn.addMember("value", "$S", confElement.getNamedQualifier());
            pb.addAnnotation(namedAnn.build());
        }
        mb.addParameter(pb.build());
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
            ann.addMember("value", "$T.class", TypeName.get(confElement.getClassedQualifier()));
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
            ann.addMember("value", "$T.class", TypeName.get(confElement.getClassedQualifier()));
            mb.addAnnotation(ann.build());
        } else if (confElement.getNamedQualifier() != null) {
            AnnotationSpec.Builder ann = AnnotationSpec.builder(Named.class);
            ann.addMember("value", "$S", confElement.getNamedQualifier());
            mb.addAnnotation(ann.build());
        }
    }

    private void generateProduceMessageConfigurable(ProducerGenerator prodGen, ConfigElement confElement) {
        TypeElement targetElm = confElement.getTarget().unwrap();
        MethodSpec.Builder mb = prodGen.addProduceMethod("get" + targetElm.getSimpleName().toString() + "With" + confElement.getImplementation().getSimpleName(),
                ClassName.bestGuess(targetElm.getQualifiedName().toString()));

        if (!confElement.getDefaultMessage()) {
            AnnotationSpec.Builder classedAnn = AnnotationSpec.builder(Classed.class);
            classedAnn.addMember("value", "$T.class", TypeName.get(confElement.getImplementation().getOriginType()));
            mb.addAnnotation(classedAnn.build());
        }

        mb.addAnnotation(Singleton.class);

        //Parameter @Classed(AConfig.class) Supplier<Service> target
        ParameterSpec.Builder targetBuilder = ParameterSpec.builder(
                ParameterizedTypeName.get(ClassName.get(Supplier.class), TypeName.get(confElement.getTarget().getOriginType())),
                FACTORY_PARAM,
                Modifier.FINAL
        );
        AnnotationSpec.Builder targetAnnBuilder = AnnotationSpec.builder(Classed.class);
        targetAnnBuilder.addMember("value", "$T.class", TypeName.get(confElement.getPrototype().getOriginType()));
        targetBuilder.addAnnotation(targetAnnBuilder.build());
        mb.addParameter(targetBuilder.build());

        //Parameter ConfImpl config
        ParameterSpec.Builder configBuilder = ParameterSpec.builder(
                TypeName.get(confElement.getImplementation().getOriginType()),
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

    private void generateInitSourceValues(ConfigElement confElement, ProducerGenerator prodGen) {
        String postProduceMethodName = "init" + confElement.getImplementation().getSimpleName();
        MethodSpec.Builder mb = prodGen.addMethod(postProduceMethodName, Modifier.PUBLIC);
        mb.addAnnotation(PostProduce.class);

        ParameterSpec.Builder confParam = ParameterSpec.builder(TypeName.get(confElement.getImplementation().asClassType().unwrap()), CONF_PARAM, Modifier.FINAL);
        confParam.addAnnotation(Message.class);
        mb.addParameter(confParam.build());

        mb.addParameter(TypeName.get(confElement.getSource().getSourceType().unwrap()), CONFIG_SOURCE_PARAM, Modifier.FINAL);

        mb.returns(TypeName.get(confElement.getImplementation().asClassType().unwrap()));
        CodeBlock.Builder cb = CodeBlock.builder();

        ArrayCodegen paramsCodegen = new ArrayCodegen();
        for (Map.Entry<String, String> param : confElement.getSource().getOptions().entrySet()) {
            paramsCodegen.add("$S", param.getKey());
            paramsCodegen.add("$S", param.getValue());
        }

        // final Connection conn = configSource.connect(Map.of(...)
        cb.add("final $T $N = $N.$N($T.of(",
                ClassName.get(ConfigSource.Connection.class),
                CONNECTION_VAR,
                CONFIG_SOURCE_PARAM,
                ConfigSource.CONNECT_METHOD,
                ClassName.get(Map.class)
        );
        cb.add(paramsCodegen.toFormat(), paramsCodegen.toValues());
        cb.add("));\n");

        // final BagType bag = conn.getValue(BagType.class)
        TypeName bagType = ClassName.bestGuess(confElement.getImplementation().getPackageName() + "." + confElement.getSource().getBagClassSimpleName());
        cb.addStatement("final $T $N = $N.$N($T.class)",
                bagType,
                BAG_VAR,
                CONNECTION_VAR,
                ConfigSource.Connection.GET_VALUE_METHOD,
                bagType
        );

        cb.addStatement("if ( $N == null ){ return $N; }", BAG_VAR, CONF_PARAM);

        for (SourceValueElement sv : confElement.getSource().getSourceValues()) {
            String fieldName = sv.getOriginField().getName();
            // if (bag.getField()!=null {config.setField(bag.getField())}
            cb.addStatement("if ( $N.$N() != null ){ $N.$N($N.$N()); }",
                    BAG_VAR,
                    "get" + StrUtils.firstCharToUpperCase(fieldName),
                    CONF_PARAM,
                    "set" + StrUtils.firstCharToUpperCase(fieldName),
                    BAG_VAR,
                    "get" + StrUtils.firstCharToUpperCase(fieldName)
            );
        }

        // close connection
        cb.addStatement("$N.$N()", CONNECTION_VAR, ConfigSource.Connection.CLOSE_METHOD);
        cb.addStatement("return $N", CONF_PARAM);
        mb.addCode(cb.build());
    }

    private void generateProducerClass(ConfigElement confElement) {
        String classSimpleName = confElement.getImplementation().getSimpleName();
        String packageName = confElement.getImplementation().getPackageName();

        ProducerGenerator prodGen = new ProducerGenerator(packageName, classSimpleName, this.getClass(), getProcessingEnv());

        // Condition
        if (confElement.getCondition() != null) {
            prodGen.addConditionAnnotation(TypeName.get(confElement.getCondition().unwrap()));
        }

        // Substitution
        if (confElement.getSubstitution() != null) {
            prodGen.addSubstitutionAnnotation(confElement.getSubstitution());
        }

        // Generates the configuration implementation producing  via annotation @Produce
        AnnotationSpec.Builder produceAnn = prodGen.addProduceAnnotation(TypeName.get(confElement.getImplementation().getOriginType()));

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
                    generateProduceMessageConfigurable(prodGen, confElement);
                    break;
                }
            }
        }

        // Init configuration source values
        if (confElement.getSource() != null) {
            produceAnn.addMember(Produce.POST_PRODUCE_METHOD, "true");
            generateInitSourceValues(confElement, prodGen);
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
