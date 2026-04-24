/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.translation.codegen.model;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.translation.Dictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Represents dictionary bean
 */
public class DictionaryElement {

    private final ClassElement originBean;
    private final String baseName;

    // Locale tag -> bundle
    private final Map<String, BundleElement> bundlesByLocale = new HashMap<>();

    /**
     * @see Dictionary#extraTranslations()
     */
    private final Set<String> extraTranslations = new HashSet<>();

    private final Set<MethodElement> keyMethods = new LinkedHashSet<>();

    public DictionaryElement(ClassElement dictionaryInterface) {

        if (dictionaryInterface == null) {
            throw new RuntimeException("Dictionary element is null");
        }
        this.originBean = dictionaryInterface;

        AnnotationAssist<Dictionary> dictAnn = dictionaryInterface.getAnnotation(Dictionary.class);
        String bName;
        if (StringUtils.isNoneBlank(dictAnn.unwrap().baseName())) {
            bName = dictAnn.unwrap().baseName();
        } else {
            bName = dictionaryInterface.packageName() + '.' + dictionaryInterface.simpleName();
            bName = bName.replace('.', '/');
        }
        this.baseName = bName;

        this.extraTranslations.addAll(Arrays.asList(dictAnn.unwrap().extraTranslations()));
    }

    public void addTranslationMethod(MethodElement keyMethod) {
        keyMethods.add(keyMethod);
    }

    public void addTranslation(MethodElement keyMethod, String languageTag, String translation) {
        BundleElement translationsBundleElement = bundlesByLocale.get(languageTag);
        if (translationsBundleElement == null) {
            translationsBundleElement = new BundleElement(this, languageTag);
            bundlesByLocale.put(languageTag, translationsBundleElement);
        }
        translationsBundleElement.addTranslation(keyMethod, translation);
    }

    public String baseName() {
        return baseName;
    }

    public ClassElement originBean() {
        return originBean;
    }

    public Set<MethodElement> keyMethods() {
        return keyMethods;
    }

    public String implClassSimpleName() {
        return originBean().simpleName() + "Impl";
    }

    public Map<String, BundleElement> bundlesByLocale() {
        return bundlesByLocale;
    }

    public Set<String> extraTranslations() {
        return extraTranslations;
    }

    @Override
    public String toString() {
        return "DictionaryBeanElement{" +
                "originBean=" + originBean.originType().toString() +
                ", baseName='" + baseName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryElement that = (DictionaryElement) o;

        return baseName.equals(that.baseName);
    }

    @Override
    public int hashCode() {
        return baseName.hashCode();
    }
}
