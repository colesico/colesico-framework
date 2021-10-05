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
import javax.lang.model.element.VariableElement;

public class FieldElement extends VarElement {

    protected final ClassElement parentClass;

    public FieldElement(ProcessingEnvironment processingEnv, ClassElement parentClass, VariableElement fieldElement) {
        super(processingEnv, fieldElement, parentClass.getMemberType(fieldElement));
        if (!originElement.getKind().isField()) {
            throw CodegenException.of().message("Unsupported element kind:" + originElement.getKind()).element(originElement).build();
        }
        this.parentClass = parentClass;
    }

    public ClassElement getParentClass() {
        return parentClass;
    }

    @Override
    public String toString() {
        return "FieldElement{" +
                "originVariableElement=" + originElement +
                '}';
    }
}
