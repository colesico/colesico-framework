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

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.ioc.codegen.model.ClassifierType;
import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.InjectableElement;
import colesico.framework.ioc.ioclet.AdvancedIoc;
import colesico.framework.ioc.ioclet.Ioclet;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.ioc.key.PPLKey;
import colesico.framework.ioc.key.TypeKey;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author Vladlen Larionov
 */
public class KeyGenerator extends FrameworkAbstractGenerator {

    public KeyGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private CodeBlock generateTypeKeyStringBased(ClassType instanceType) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S)", ClassName.get(TypeKey.class), instanceType.unwrap().toString());
        return codeBlock.build();
    }

    private CodeBlock generateTypeKeyClassBased(ClassType instanceType) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($T.class)", ClassName.get(TypeKey.class), TypeName.get(instanceType.getErasure()));
        return codeBlock.build();
    }

    private CodeBlock generateNamedKeyStringBased(ClassType instanceType, String name) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S,$S)", ClassName.get(NamedKey.class), instanceType.getName(), name);
        return codeBlock.build();
    }

    private CodeBlock generateClassedKeyClassBased(ClassType instanceType, ClassifierType classifier) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($T.class,$T.class)",
                ClassName.get(ClassedKey.class), TypeName.get(instanceType.getErasure()), TypeName.get(classifier.getErasure()));
        return codeBlock.build();
    }

    private CodeBlock generateClassedKeyStringBased(ClassType instanceType, ClassifierType classifier) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S,$S)",
                ClassName.get(ClassedKey.class), instanceType.getName(), classifier.getErasure().toString()
        );
        return codeBlock.build();
    }

    private CodeBlock generatePPLKeyStringBased(ClassType instanceType, String withNamed, ClassifierType withClassed) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        if (withClassed != null) {
            codeBlock.add("new $T($S,$S,$S)",
                    ClassName.get(PPLKey.class),
                    instanceType.getName(), withNamed, withClassed.getErasure().toString());
        } else {
            codeBlock.add("new $T($S,$S,null)",
                    ClassName.get(PPLKey.class),
                    instanceType.getName(), withNamed);
        }
        return codeBlock.build();
    }

    private CodeBlock generatePPLKeyClassBased(ClassType instanceType, String withNamed, ClassifierType withClassed) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        if (withClassed != null) {
            codeBlock.add("new $T($T.class,$S,$T.class)",
                    ClassName.get(PPLKey.class),
                    TypeName.get(instanceType.getErasure()), withNamed, TypeName.get(withClassed.getErasure()));
        } else {
            codeBlock.add("new $T($T.class,$S,null)",
                    ClassName.get(PPLKey.class),
                    TypeName.get(instanceType.getErasure()), withNamed);
        }
        return codeBlock.build();
    }

    /**
     * Code for use at factory registration stage and scoped factory
     *
     * @param factory
     * @return
     * @see Ioclet#addFactories
     */
    public CodeBlock forFactory(FactoryElement factory, ClassType suppliedType) {
        if (suppliedType==null){
            suppliedType =factory.getSuppliedType();
        }
        if (factory.getPostProduce() != null) {
            return generatePPLKeyStringBased(suppliedType,
                    factory.getPostProduce().getWithNamed(), factory.getPostProduce().getWithClassed());
        } else if (factory.getClassed() != null) {
            return generateClassedKeyStringBased(suppliedType, factory.getClassed());
        } else if (factory.getNamed() != null) {
            return generateNamedKeyStringBased(suppliedType, factory.getNamed());
        } else {
            return generateTypeKeyStringBased(suppliedType);
        }
    }

    /**
     * This code is used to obtain factory of dependencies
     * (in factory setup method or before instance creation)
     *
     * @param param
     * @return
     * @see colesico.framework.ioc.ioclet.Factory#setup(AdvancedIoc)
     */
    public CodeBlock forInjection(InjectableElement param) {
        if (param.getClassed() != null) {
            return generateClassedKeyStringBased(param.getInjectedType(), param.getClassed());
        } else if (param.getNamed() != null) {
            return generateNamedKeyStringBased(param.getInjectedType(), param.getNamed());
        } else {
            return generateTypeKeyStringBased(param.getInjectedType());
        }
    }

    /**
     * Returns code for post process listener obtaining form ioc
     *
     * @param instanceType
     * @return
     */
    public CodeBlock forPostProduceListener(ClassType instanceType, String withNamed, ClassifierType withClassed) {
        return generatePPLKeyClassBased(instanceType, withNamed, withClassed);
    }

    public CodeBlock forScope(ClassType scopeType) {
        return generateTypeKeyClassBased(scopeType);
    }
}
