package colesico.framework.translation.codegen.model;

import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.translation.Dictionary;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents dictionary bean
 */
public class DictionaryElement {

    private final ClassElement originBean;
    private final String basePath;
    private final Map<String, BundleElement> bundlesByLocale = new HashMap<>();
    private final Set<MethodElement> keyMethods = new LinkedHashSet<>();

    public DictionaryElement(ClassElement dictionaryInterface) {

        if (dictionaryInterface == null) {
            throw new RuntimeException("Dictionary element is null");
        }
        this.originBean = dictionaryInterface;

        AnnotationElement<Dictionary> beanAnn = dictionaryInterface.getAnnotation(Dictionary.class);
        String bPath;
        if (StringUtils.isNoneBlank(beanAnn.unwrap().basePath())) {
            bPath = beanAnn.unwrap().basePath();
        } else {
            bPath = dictionaryInterface.getPackageName() + '.' + dictionaryInterface.getSimpleName();
            bPath = bPath.replace('.', '/');
        }
        this.basePath = bPath;

    }

    public void addTranslationMethod(MethodElement keyMethod) {
        keyMethods.add(keyMethod);
    }

    public void addTranslation(MethodElement keyMethod, String localeKey, String translation) {
        BundleElement translationsBundleElement = bundlesByLocale.get(localeKey);
        if (translationsBundleElement == null) {
            translationsBundleElement = new BundleElement(this, localeKey);
            bundlesByLocale.put(localeKey, translationsBundleElement);
        }
        translationsBundleElement.addTranslation(keyMethod, translation);
    }

    public String getBasePath() {
        return basePath;
    }

    public ClassElement getOriginBean() {
        return originBean;
    }

    public Set<MethodElement> getKeyMethods() {
        return keyMethods;
    }

    public String getImplClassSimpleName() {
        return getOriginBean().getSimpleName().toString() + "Impl";
    }

    public Map<String, BundleElement> getBundlesByLocale() {
        return bundlesByLocale;
    }

    @Override
    public String toString() {
        return "DictionaryBeanElement{" +
                "originBean=" + originBean.asType().toString() +
                ", basePath='" + basePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryElement that = (DictionaryElement) o;

        return basePath.equals(that.basePath);
    }

    @Override
    public int hashCode() {
        return basePath.hashCode();
    }
}
