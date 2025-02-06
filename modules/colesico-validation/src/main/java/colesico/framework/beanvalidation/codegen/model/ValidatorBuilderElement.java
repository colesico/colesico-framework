package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.beanvalidation.ValidatorBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Validator Builder Prototype element
 *
 * @see ValidatorBuilder
 */
public class ValidatorBuilderElement {

    /**
     * Parent bean element (marked with {@link ValidatorBuilder})
     */
    private ValidatedBeanElement parentBean;

    /**
     * Validator builder prototype  name
     *
     * @see ValidatorBuilder#name()
     */
    private final String name;

    /**
     * @see ValidatorBuilder#sequence()
     */
    private final ClassType sequence;

    /**
     * Validator builder prototype package name
     */
    private final String packageName;

    private final ClassType superclass;

    private final List<ValidatedPropertyElement> properties = new ArrayList<>();

    public ValidatorBuilderElement(String name, ClassType sequence, String packageName, ClassType superclass) {
        this.name = name;
        this.sequence = sequence;
        this.packageName = packageName;
        this.superclass = superclass;
    }

    public void addProperty(ValidatedPropertyElement property) {
        properties.add(property);
        property.setParentBuilder(this);
    }

    /**
     *  Validator builder class simple name
     */
    public String getClassSimpleName() {
        return parentBean.getOriginType().asClassElement().getSimpleName() + StrUtils.firstCharToUpperCase(name);
    }

    public List<ValidatedPropertyElement> getProperties() {
        return properties;
    }

    public ValidatedBeanElement getParentBean() {
        return parentBean;
    }

    public void setParentBean(ValidatedBeanElement parentBean) {
        this.parentBean = parentBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public ClassType getSuperclass() {
        return superclass;
    }

    @Override
    public String toString() {
        return "ValidatorBuilderElement{" +
                "parentBean=" + parentBean +
                ", name='" + name + '\'' +
                ", sequence=" + sequence +
                ", packageName='" + packageName + '\'' +
                ", superclass=" + superclass +
                ", properties=" + properties +
                '}';
    }
}
