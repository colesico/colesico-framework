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

import colesico.framework.ioc.ClassedKey;
import colesico.framework.ioc.NamedKey;
import colesico.framework.ioc.TypeKey;
import colesico.framework.ioc.codegen.model.FactoryElement;
import colesico.framework.ioc.codegen.model.InjectableElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.type.DeclaredType;

/**
 * @author Vladlen Larionov
 */
public class KeyGenerator {

    private CodeBlock generateTypeKeyStringBased(String className) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S)", ClassName.get(TypeKey.class), className);
        return codeBlock.build();
    }

    private CodeBlock generateTypeKeyClassBased(String className) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($T.class)", ClassName.get(TypeKey.class), ClassName.bestGuess(className));
        return codeBlock.build();
    }

    private CodeBlock generateNamedKeyStringBased(String className, String name) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S,$S)", ClassName.get(NamedKey.class), className, name);
        return codeBlock.build();
    }

    private CodeBlock generateClassedKeyStringBased(String className, String classifierClassName) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($S,$S)", ClassName.get(ClassedKey.class), className, classifierClassName);
        return codeBlock.build();
    }

    private CodeBlock generateClassedKeyClassBased(String className, String classifierClassName) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T($T.class,$T.class)",
                ClassName.get(ClassedKey.class), ClassName.bestGuess(className),ClassName.bestGuess(classifierClassName));
        return codeBlock.build();
    }

    public CodeBlock forFactory(FactoryElement factory) {
        if (factory.getClassed() != null) {
            return generateClassedKeyStringBased(factory.getSuppliedType().toString(), factory.getClassed());
        } else if (factory.getNamed() != null) {
            return generateNamedKeyStringBased(factory.getSuppliedType().toString(), factory.getNamed());
        } else {
            return generateTypeKeyStringBased(factory.getSuppliedType().toString());
        }
    }

    public CodeBlock forInjection(InjectableElement param) {
        DeclaredType paramType = param.getInjectedType();
        String className = paramType.asElement().toString();

        if (param.getClassed() != null) {
            return generateClassedKeyClassBased(className, param.getClassed());
        } else if (param.getNamed() != null) {
            return generateNamedKeyStringBased(className, param.getNamed());
        } else {
            return generateTypeKeyClassBased(className);
        }
    }

    public CodeBlock forScope(String scopeClassName) {
        return generateTypeKeyClassBased(scopeClassName);
    }
}
