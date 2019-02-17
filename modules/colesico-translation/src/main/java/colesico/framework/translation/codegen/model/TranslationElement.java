package colesico.framework.translation.codegen.model;

import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.translation.TranslationKey;

import javax.lang.model.element.ExecutableElement;

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
        AnnotationElement<TranslationKey> tk = keyMethod.getAnnotation(TranslationKey.class);
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

