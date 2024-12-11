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

package colesico.framework.ioc.codegen.generator;

import colesico.framework.assist.LazySingleton;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.IocletElement;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.ioclet.Catalog;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.ioclet.Ioclet;
import com.palantir.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class IocletGenerator extends FrameworkAbstractGenerator {
    public static final String PRODUCER_FIELD = "producer";

    private static final Logger log = LoggerFactory.getLogger(IocletGenerator.class);

    protected final FactoryGenerator factoryGenerator;
    protected final KeyGenerator keyGenerator;

    protected IocletElement iocletElement;
    protected TypeSpec.Builder classBuilder;

    public IocletGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.factoryGenerator = new FactoryGenerator(processingEnv);
        this.keyGenerator = new KeyGenerator(processingEnv);
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

    protected void generateGetProducerIdMethod() {
        //log.info("Generate  method: "+Ioclet.GET_PRODUCER_NAME_METHOD);
        MethodSpec.Builder mb = MethodSpec.methodBuilder(Ioclet.GET_ID_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(String.class));
        mb.addAnnotation(Override.class);
        mb.addStatement("return $S", iocletElement.getIocletId());
        classBuilder.addMethod(mb.build());
    }

    protected void generateSupplierFactoryMethods() {
        for (FactoryElement fe : iocletElement.getFactories()) {
            MethodSpec.Builder mb = MethodSpec.methodBuilder(fe.getFactoryMethodName());

            if (fe.getPostProduce() == null) {
                mb.addJavadoc("Factory to produce " + fe.getSuppliedType().unwrap().toString() + " class instance\n");
                mb.addJavadoc("Scope: " + fe.getScope().getKind() + "; Custom: " + fe.getScope().getCustomScopeClass() + '\n');
                if (fe.getNamed() != null) {
                    mb.addJavadoc("Named: " + fe.getNamed() + '\n');
                }
                if (fe.getClassed() != null) {
                    mb.addJavadoc("Classed: " + fe.getClassed().getErasure().toString() + '\n');
                }
                if (fe.getPolyproduce()) {
                    mb.addJavadoc("Polyproduce: true" + '\n');
                }
            } else {
                mb.addJavadoc("Factory of post produce listener for class " + fe.getSuppliedType().asClassElement().getName() + '\n');
                mb.addJavadoc("WithNamed: " + fe.getPostProduce().getWithNamed() + '\n');
                mb.addJavadoc("WithClassed: " + fe.getPostProduce().getWithClassed() + '\n');
            }

            mb.returns(ParameterizedTypeName.get(
                            ClassName.get(Factory.class),
                            TypeName.get(fe.getSuppliedType().unwrap())
                    )
            );
            mb.addModifiers(Modifier.PUBLIC);
            mb.addStatement("return $L", factoryGenerator.generateFactory(fe));
            classBuilder.addMethod(mb.build());
        }
    }

    protected void generateAddFactoriesMethod() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(Ioclet.ADD_FACTORIES_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.addParameter(ClassName.get(Catalog.class), Ioclet.CATALOG_PARAM, Modifier.FINAL);
        mb.addAnnotation(Override.class);
        for (FactoryElement factoryElm : iocletElement.getFactories()) {

            // All supplied types by given factory: type itself and supertypes
            List<ClassType> suppliedTypeList = new ArrayList<>();
            suppliedTypeList.addAll(factoryElm.getKeyTypes());
            if (suppliedTypeList.isEmpty()) {
                suppliedTypeList.add(factoryElm.getSuppliedType());
            }

            // Loop for all factory supplied types  (supplied type itself and supertypes)
            for (ClassType suppliedType : suppliedTypeList) {
                CodeBlock.Builder cb = CodeBlock.builder();
                // if (catalog.accept(
                cb.add("if($N.$N(", Ioclet.CATALOG_PARAM, Catalog.ACCEPT_METHOD);

                // new Key(), condition, substitute, polyproduce
                cb.add(keyGenerator.forFactory(factoryElm, suppliedType));
                if (factoryElm.getCondition() != null) {
                    cb.add(", new $T()", TypeName.get(factoryElm.getCondition().getConditionClass().unwrap()));
                } else {
                    cb.add(", null");
                }

                // Substitution
                if (factoryElm.getSubstitution() != null) {
                    cb.add(", $T.$N", ClassName.get(Substitution.class), factoryElm.getSubstitution().getSubstitutionType().name());
                } else {
                    cb.add(", $T.$N", ClassName.get(Substitution.class), Substitution.REGULAR.name());
                }

                // Polyproduce
                cb.add(", $L", factoryElm.getPolyproduce());

                cb.add(")){\n");

                // catalog.add(factoryX())
                cb.indent();
                cb.addStatement("$N.$N($N())", Ioclet.CATALOG_PARAM, Catalog.ADD_METHOD, factoryElm.getFactoryMethodName());
                cb.unindent();
                cb.add("}\n\n");
                mb.addCode(cb.build());
            }
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

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null, "Producer: " + iocletElement.getOriginProducer().getName());
        classBuilder.addAnnotation(genstamp);

        generateProducerField();
        generateGetProducerIdMethod();
        generateSupplierFactoryMethods();
        generateAddFactoriesMethod();

        return classBuilder.build();
    }
}
