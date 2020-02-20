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

package colesico.framework.assist.codegen.model;

import colesico.framework.assist.codegen.CodegenException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public class ClassType extends ParserType {

    protected final DeclaredType originDeclaredType;

    public ClassType(ProcessingEnvironment processingEnv, DeclaredType classType) {
        super(processingEnv);
        if (classType == null) {
            throw CodegenException.of().message("classType is null").build();
        }
        this.originDeclaredType = classType;
    }

    public static ClassType fromElement(ProcessingEnvironment processingEnv, TypeElement typeElement) {
        if (typeElement == null) {
            throw CodegenException.of().message("typeElement is null").build();
        }
        return new ClassType(processingEnv, (DeclaredType) typeElement.asType());
    }

    @Override
    public DeclaredType unwrap() {
        return originDeclaredType;
    }

    public TypeElement asElement() {
        return (TypeElement) originDeclaredType.asElement();
    }

    public ClassElement asClassElement() {
        return new ClassElement(getProcessingEnv(), asElement());
    }

    public DeclaredType getErasure() {
        return (DeclaredType) getTypeUtils().erasure(originDeclaredType);
    }

    public TypeMirror getMemberType(Element element) {
        return getTypeUtils().asMemberOf(originDeclaredType, element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassType classType = (ClassType) o;

        return Objects.equals(originDeclaredType, classType.originDeclaredType);
    }

    @Override
    public int hashCode() {
        return originDeclaredType != null ? originDeclaredType.hashCode() : 0;
    }
}
