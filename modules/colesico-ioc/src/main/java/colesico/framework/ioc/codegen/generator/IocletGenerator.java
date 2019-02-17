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

package colesico.framework.ioc.codegen.generator;

import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.IocletElement;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.ioclet.Catalog;
import colesico.framework.ioc.ioclet.Ioclet;
import colesico.framework.assist.LazySingleton;
import colesico.framework.assist.codegen.CodegenUtils;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;

/**
 * @author Vladlen Larionov
 */
public class IocletGenerator {
    public static final String PRODUCER_FIELD = "producer";

    private static final Logger log = LoggerFactory.getLogger(IocletGenerator.class);

    protected final FactoryGenerator factoryGenerator;
    protected final KeyGenerator keyGenerator;


    protected IocletElement iocletElement;
    protected TypeSpec.Builder classBuilder;

    public IocletGenerator() {
        this.factoryGenerator = new FactoryGenerator();
        this.keyGenerator = new KeyGenerator();
    }

    protected void generateProducerField() {
        //log.info("Generate field: "+PRODUCER_FIELD);
        TypeName producerTypeName = TypeName.get(iocletElement.getOriginProducer().asClassType().unwrap());

        TypeName fieldType = ParameterizedTypeName.get(ClassName.get(LazySingleton.class), producerTypeName);

        FieldSpec.Builder fb = FieldSpec.builder(fieldType, PRODUCER_FIELD);
        fb.addModifiers(Modifier.PRIVATE, Modifier.FINAL);

        MethodSpec.Builder mb = MethodSpec.methodBuilder(LazySingleton.CREATE_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        mb.returns(producerTypeName);
        mb.addStatement("return new $T()", producerTypeName);
        mb.addAnnotation(Override.class);

        TypeSpec.Builder tb = TypeSpec.anonymousClassBuilder("");
        tb.superclass(fieldType);
        tb.addMethod(mb.build());


        fb.initializer("$L", tb.build());

        classBuilder.addField(fb.build());
    }

    protected void generateGetRankMethod() {
        //log.info("Generate  method: "+Ioclet.GET_RANK_METHOD);
        MethodSpec.Builder mb = MethodSpec.methodBuilder(Ioclet.GET_RANK_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(String.class));
        mb.addAnnotation(Override.class);
        mb.addStatement("return $S", iocletElement.getRank());
        classBuilder.addMethod(mb.build());
    }

    protected void generateGetProducerIdMethod() {
        //log.info("Generate  method: "+Ioclet.GET_PRODUCER_NAME_METHOD);
        MethodSpec.Builder mb = MethodSpec.methodBuilder(Ioclet.GET_PRODUCER_ID_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(String.class));
        mb.addAnnotation(Override.class);
        mb.addStatement("return $S", iocletElement.getProducerId());
        classBuilder.addMethod(mb.build());
    }

    protected void generateSuplierFactoryMethods() {
        for (FactoryElement sme : iocletElement.getFactories()) {
            MethodSpec.Builder mb = MethodSpec.methodBuilder(sme.getFactoryMethodName());
            mb.returns(ParameterizedTypeName.get(
                    ClassName.get(Factory.class),
                    TypeName.get(sme.getSuppliedType().getErasure())
                    )
            );
            mb.addModifiers(Modifier.PUBLIC);
            mb.addStatement("return $L", factoryGenerator.generateFactory(sme));
            classBuilder.addMethod(mb.build());
        }
    }

    protected void generateAddFactoriesMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(Ioclet.ADD_FACTORIES_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.addParameter(ClassName.get(Catalog.class), Ioclet.CATALOG_PARAM, Modifier.FINAL);
        mb.addAnnotation(Override.class);
        for (FactoryElement spl : iocletElement.getFactories()) {
            CodeBlock.Builder cb = CodeBlock.builder();
            cb.add("if($N.$N(", Ioclet.CATALOG_PARAM, Catalog.ACCEPT_METHOD);

            // Catalog.Entry.of(new Key(...),true|false)
            cb.add("$T.$N(", ClassName.get(Catalog.Entry.class), Catalog.Entry.OF_METHOD);
            cb.add(keyGenerator.forFactory(spl));
            cb.add(",$L)", spl.getPolyproduce().booleanValue());

            cb.add(")){\n");
            cb.indent();
            cb.addStatement("$N.$N($N())", Ioclet.CATALOG_PARAM, Catalog.ADD_METHOD, spl.getFactoryMethodName());
            cb.unindent();
            cb.add("}\n\n");
            mb.addCode(cb.build());
        }
        classBuilder.addMethod(mb.build());
    }

    public TypeSpec generate(IocletElement iocletElement) {
        log.debug("Generate Ioclet based on: " + iocletElement);
        this.iocletElement = iocletElement;

        classBuilder = TypeSpec.classBuilder(iocletElement.getIocletClassSimpleName());
        classBuilder.addSuperinterface(ClassName.get(Ioclet.class));
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Producer: " + iocletElement.getOriginProducer().toString());
        classBuilder.addAnnotation(genstamp);

        generateProducerField();
        generateGetProducerIdMethod();
        generateGetRankMethod();
        generateSuplierFactoryMethods();
        generateAddFactoriesMethod();

        return classBuilder.build();
    }
}
