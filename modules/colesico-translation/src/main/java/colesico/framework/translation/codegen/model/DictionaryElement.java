package colesico.framework.translation.codegen.model;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.translation.Dictionary;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DictionaryElement {
    private final TypeElement originBean;
    private final String basePath;
    private final Map<String, BundleElement> bundlesByLocale = new HashMap<>();
    private final Set<ExecutableElement> keyMethods = new LinkedHashSet<>();

    public DictionaryElement(TypeElement dictionaryInterface) {

        if (dictionaryInterface == null) {
            throw new RuntimeException("Dictionary element is null");
        }
        this.originBean = dictionaryInterface;

        Dictionary beanAnn = dictionaryInterface.getAnnotation(Dictionary.class);
        String bPath;
        if (StringUtils.isNoneBlank(beanAnn.basePath())) {
            bPath = beanAnn.basePath();
        } else {
            bPath = CodegenUtils.getPackageName(dictionaryInterface) + '.' + dictionaryInterface.getSimpleName().toString();
            bPath = bPath.replace('.', '/');
        }
        this.basePath = bPath;

    }

    public void addTranslationMethod(ExecutableElement keyMethod) {
        keyMethods.add(keyMethod);
    }

    public void addTranslation(ExecutableElement keyMethod, String localeKey, String translation) {
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

    public TypeElement getOriginBean() {
        return originBean;
    }

    public Set<ExecutableElement> getKeyMethods() {
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
