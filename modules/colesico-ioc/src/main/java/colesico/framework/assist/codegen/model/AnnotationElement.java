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
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class AnnotationElement extends ParserElement {

    private final TypeElement originAnnotation;

    public AnnotationElement(ProcessingEnvironment processingEnv, TypeElement originAnnotation) {
        super(processingEnv);
        this.originAnnotation = originAnnotation;
    }

    @Override
    public Element unwrap() {
        return originAnnotation;
    }

    @Override
    public TypeMirror getOriginType() {
        return originAnnotation.asType();
    }
}
