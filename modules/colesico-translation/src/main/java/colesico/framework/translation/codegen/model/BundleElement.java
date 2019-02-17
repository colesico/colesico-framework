package colesico.framework.translation.codegen.model;

import colesico.framework.assist.codegen.model.MethodElement;

import javax.lang.model.element.ExecutableElement;
import java.util.LinkedHashSet;
import java.util.Set;

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
