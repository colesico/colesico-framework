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

    public ParameterElement(ProcessingEnvironment processingEnv, MethodElement parentMethod, VariableElement parameterElement, TypeMirror parameterType) {
        super(processingEnv, parameterElement, parameterType);
        this.parentMethod = parentMethod;
    }

    public MethodElement getParentMethod() {
        return parentMethod;
    }

    @Override
    public String toString() {
        return "ParameterElement{" +
                "originElement=" + originElement +
                ", originType=" + originType +
                '}';
    }
}
