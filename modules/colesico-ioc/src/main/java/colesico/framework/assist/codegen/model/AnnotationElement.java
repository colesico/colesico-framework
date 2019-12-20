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
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AnnotationElement<A extends Annotation> extends Toolbox {

    protected final A originAnnotation;

    public AnnotationElement(ProcessingEnvironment processingEnv, A annotation) {
        super(processingEnv);
        this.originAnnotation = annotation;
    }

    public A unwrap() {
        return originAnnotation;
    }

    public TypeMirror getValueTypeMirror(Consumer<A> fieldAccessor) {
        TypeMirror typeMirror = null;
        try {
            fieldAccessor.accept(originAnnotation);
        } catch (MirroredTypeException mte) {
            typeMirror = mte.getTypeMirror();
        }
        return typeMirror;
    }

    public TypeMirror[] getValueTypeMirrors(Consumer<A> fieldAccessor) {
        try {
            fieldAccessor.accept(originAnnotation);
        } catch (javax.lang.model.type.MirroredTypesException mte) {
            List<? extends TypeMirror> typeMirrors = mte.getTypeMirrors();
            TypeMirror[] result = new TypeMirror[typeMirrors.size()];
            for (int i = 0; i < typeMirrors.size(); i++) {
                result[i] = typeMirrors.get(i);
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationElement<?> that = (AnnotationElement<?>) o;

        return Objects.equals(originAnnotation, that.originAnnotation);
    }

    @Override
    public int hashCode() {
        return originAnnotation != null ? originAnnotation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AnnotationElement{" +
                "originAnnotation=" + originAnnotation +
                '}';
    }
}
