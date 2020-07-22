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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ClassType extends ParserType {

    protected final DeclaredType originType;

    public ClassType(ProcessingEnvironment processingEnv, DeclaredType classType) {
        super(processingEnv);
        if (classType == null) {
            throw CodegenException.of().message("classType is null").build();
        }
        this.originType = classType;
    }

    public static ClassType fromElement(ProcessingEnvironment processingEnv, TypeElement typeElement) {
        if (typeElement == null) {
            throw CodegenException.of().message("typeElement is null").build();
        }
        return new ClassType(processingEnv, (DeclaredType) typeElement.asType());
    }

    @Override
    public DeclaredType unwrap() {
        return originType;
    }

    public TypeElement asTypeElement() {
        return (TypeElement) originType.asElement();
    }

    public ClassElement asClassElement() {
        return ClassElement.fromType(getProcessingEnv(), originType);
    }

    public DeclaredType getErasure() {
        return (DeclaredType) getTypeUtils().erasure(originType);
    }

    public TypeMirror getMemberType(Element element) {
        return getTypeUtils().asMemberOf(originType, element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassType classType = (ClassType) o;

        return Objects.equals(originType, classType.originType);
    }

    @Override
    public int hashCode() {
        return originType != null ? originType.hashCode() : 0;
    }
}
