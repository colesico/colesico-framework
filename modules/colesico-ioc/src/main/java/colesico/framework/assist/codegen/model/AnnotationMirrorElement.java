/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMirrorElement extends ParserElement {

    protected final AnnotationMirror originAnnotationMirror;

    public AnnotationMirrorElement(ProcessingEnvironment processingEnv, AnnotationMirror annotationMirror) {
        super(processingEnv);
        this.originAnnotationMirror = annotationMirror;
    }

    @Override
    public TypeElement unwrap() {
        return (TypeElement) originAnnotationMirror.getAnnotationType().asElement();
    }

    public DeclaredType getType() {
        return originAnnotationMirror.getAnnotationType();
    }

    public AnnotationValue getValue(String fieldName) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : originAnnotationMirror.getElementValues().entrySet()) {
            if (fieldName.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Map<? extends ExecutableElement, ? extends AnnotationValue> getValuesWithDefaults() {
        Map<ExecutableElement, AnnotationValue> valMap = new HashMap<>();
        if (originAnnotationMirror.getElementValues() != null) {
            valMap.putAll(originAnnotationMirror.getElementValues());
        }
        for (ExecutableElement meth : ElementFilter.methodsIn(originAnnotationMirror.getAnnotationType().asElement().getEnclosedElements())) {
            AnnotationValue defaultValue = meth.getDefaultValue();
            if (defaultValue != null && !valMap.containsKey(meth)) {
                valMap.put(meth, defaultValue);
            }
        }
        return valMap;
    }
}
