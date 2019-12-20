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

import colesico.framework.assist.codegen.model.MethodElement;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents translations pairs  (key=translation) for the given locale
 */
public class BundleElement {

    private final DictionaryElement parentDictionary;
    private final String localeKey;

    private final Set<TranslationElement> translations = new LinkedHashSet<>();

    public BundleElement(DictionaryElement parentDictionary, String localeKey) {
        this.parentDictionary = parentDictionary;
        this.localeKey = localeKey;
    }

    public void addTranslation(MethodElement keyMethod, String translation) {
        TranslationElement translationElement = new TranslationElement(this, keyMethod, translation);
        translations.add(translationElement);
    }

    public DictionaryElement getParentDictionary() {
        return parentDictionary;
    }

    public String getLocaleKey() {
        return localeKey;
    }

    public Set<TranslationElement> getTranslations() {
        return translations;
    }

    @Override
    public String toString() {
        return "TranslationsBundleElement{" +
            " DictionaryBean=" + parentDictionary.getOriginBean().asType().toString() +
            ", Locale='" + localeKey + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BundleElement that = (BundleElement) o;

        if (!parentDictionary.equals(that.parentDictionary)) return false;
        return localeKey.equals(that.localeKey);
    }

    @Override
    public int hashCode() {
        int result = parentDictionary.hashCode();
        result = 31 * result + localeKey.hashCode();
        return result;
    }
}
