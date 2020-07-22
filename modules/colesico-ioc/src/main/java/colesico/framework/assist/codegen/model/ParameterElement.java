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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public class ParameterElement extends VarElement {

    protected final MethodElement parentMethod;
    protected final VariableElement originElement;
    protected final TypeMirror originType;

    public ParameterElement(ProcessingEnvironment processingEnv, MethodElement parentMethod, VariableElement parameterElement, TypeMirror parameterType) {
        super(processingEnv);
        this.parentMethod = parentMethod;
        this.originElement = parameterElement;
        this.originType = parameterType;
    }

    @Override
    public VariableElement unwrap() {
        return originElement;
    }

    @Override
    public String getName() {
        return originElement.getSimpleName().toString();
    }

    @Override
    public TypeMirror asTypeMirror() {
        return originType;
    }

    @Override
    public ClassType asClassType() {
        if (originType.getKind() != TypeKind.DECLARED) {
            return null;
        }
        return new ClassType(getProcessingEnv(), (DeclaredType) originType);
    }

    public MethodElement getParentMethod() {
        return parentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterElement that = (ParameterElement) o;

        return Objects.equals(originElement, that.originElement);
    }

    @Override
    public int hashCode() {
        return originElement != null ? originElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ParameterElement{" +
                "originVariableElement=" + originElement +
                ", originTypeMirror=" + originType +
                '}';
    }
}
