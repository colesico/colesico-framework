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
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.InjectableElement;
import colesico.framework.ioc.ioclet.AdvancedIoc;
import colesico.framework.ioc.ioclet.Ioclet;
import colesico.framework.ioc.key.PPLKey;
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
        codeBlock.add("new $T($S)", ClassName.get(TypeKey.class), instanceType.getErasure().toString());
        return codeBlock.build();
    }

    private CodeBlock generateTypeKeyClassBased(ClassType instanceType) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($T.class)", ClassName.get(TypeKey.class), TypeName.get(instanceType.getErasure()));
        return codeBlock.build();
    }

    private CodeBlock generateNamedKeyStringBased(ClassType instanceType, String name) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S,$S)", ClassName.get(NamedKey.class), instanceType.getErasure().toString(), name);
        return codeBlock.build();
    }

    private CodeBlock generateClassedKeyStringBased(ClassType instanceType, ClassType classifier) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S,$S)",
            ClassName.get(ClassedKey.class), instanceType.getErasure().toString(), classifier.getErasure().toString()
        );
        return codeBlock.build();
    }

    private CodeBlock generateClassedKeyClassBased(ClassType instanceType, ClassType classifier) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($T.class,$T.class)",
            ClassName.get(ClassedKey.class), TypeName.get(instanceType.getErasure()), TypeName.get(classifier.getErasure()));
        return codeBlock.build();
    }

    private CodeBlock generatePPLKeyStringBased(ClassType instanceType, String withNamed, ClassType withClassed) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        if (withClassed!=null) {
            codeBlock.add("new $T($S,$S,$S)",
                ClassName.get(PPLKey.class),
                instanceType.getErasure().toString(), withNamed, withClassed.getErasure().toString());
        } else {
            codeBlock.add("new $T($S,$S,null)",
                ClassName.get(PPLKey.class),
                instanceType.getErasure().toString(), withNamed);
        }
        return codeBlock.build();
    }

    private CodeBlock generatePPLKeyClassBased(ClassType instanceType, String withNamed, ClassType withClassed) {
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
     * Code for use at factory registration stage
     *
     * @param factory
     * @return
     * @see Ioclet#addFactories
     */
    public CodeBlock forFactory(FactoryElement factory) {
        if (factory.getPostProduce() != null) {
            return generatePPLKeyStringBased(factory.getSuppliedType(),
                factory.getPostProduce().getWithNamed(), factory.getPostProduce().getWithClassed());
        } else if (factory.getClassed() != null) {
            return generateClassedKeyStringBased(factory.getSuppliedType(), factory.getClassed());
        } else if (factory.getNamed() != null) {
            return generateNamedKeyStringBased(factory.getSuppliedType(), factory.getNamed());
        } else {
            return generateTypeKeyStringBased(factory.getSuppliedType());
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
            return generateClassedKeyClassBased(param.getInjectedType(), param.getClassed());
        } else if (param.getNamed() != null) {
            return generateNamedKeyStringBased(param.getInjectedType(), param.getNamed());
        } else {
            return generateTypeKeyClassBased(param.getInjectedType());
        }
    }

    /**
     * Returns code for post process listener obtaining form ioc
     *
     * @param instanceType
     * @return
     */
    public CodeBlock forPostProduceListener(ClassType instanceType, String withNamed, ClassType withClassed) {
        return generatePPLKeyClassBased(instanceType, withNamed, withClassed);
    }

    public CodeBlock forScope(ClassType scopeType) {
        return generateTypeKeyClassBased(scopeType);
    }
}
