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
import javax.lang.model.type.TypeMirror;

abstract public class VarElement extends ParserElement {

    protected final VariableElement originElement;
    protected final TypeMirror originType;

    public VarElement(ProcessingEnvironment processingEnv, VariableElement originElement, TypeMirror originType) {
        super(processingEnv);
        this.originElement = originElement;
        this.originType = originType;
    }

    @Override
    public VariableElement unwrap() {
        return originElement;
    }

    @Override
    public TypeMirror getOriginType() {
        return originType;
    }

}
