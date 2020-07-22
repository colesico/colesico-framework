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

package colesico.framework.translation.codegen.model;

import colesico.framework.assist.codegen.model.AnnotationAtom;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.translation.TranslationKey;

public class TranslationElement {
    private final BundleElement parentDictionary;
    private final MethodElement keyMethod;
    private final String translation;

    public TranslationElement(BundleElement parentDictionary, MethodElement keyMethod, String translation) {
        this.parentDictionary = parentDictionary;
        this.keyMethod = keyMethod;
        this.translation = translation;
    }

    public BundleElement getParentDictionary() {
        return parentDictionary;
    }

    public MethodElement getKeyMethod() {
        return keyMethod;
    }

    public String getTranslation() {
        return translation;
    }

    public String getTranslationKey() {
        AnnotationAtom<TranslationKey> tk = keyMethod.getAnnotation(TranslationKey.class);
        if (tk != null) {
            return tk.unwrap().value();
        } else {
            //return StrUtils.firstCharToUpperCase(keyMethod.getSimpleName().toString());
            return keyMethod.getName();
        }
    }

    @Override
    public String toString() {
        return "TranslationElement{ method=" + keyMethod.getName() + ", translation='" + translation + "' }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationElement that = (TranslationElement) o;

        if (!parentDictionary.equals(that.parentDictionary)) return false;
        if (!keyMethod.equals(that.keyMethod)) return false;
        return translation.equals(that.translation);
    }

    @Override
    public int hashCode() {
        int result = parentDictionary.hashCode();
        result = 31 * result + keyMethod.hashCode();
        result = 31 * result + translation.hashCode();
        return result;
    }
}

