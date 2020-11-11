package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Validator Builder Prototype element
 */
public class ValidatorBuilderElement {

    /**
     * Parent validated bean element
     */
    private ValidatedBeanElement parentBean;

    /**
     * Validator builder prototype package name
     */
    private final String packageName;

    /**
     * Validator builder prototype class name
     */
    private final String className;

    private final ClassType extendsClass;

    private final List<ValidatedPropertyElement> properties = new ArrayList<>();

    public ValidatorBuilderElement(String packageName, String className, ClassType extendsClass) {
        this.packageName = packageName;
        this.className = className;
        this.extendsClass = extendsClass;
    }

    public void addProperty(ValidatedPropertyElement property) {
        properties.add(property);
        property.setParentBuilder(this);
    }

    public String getBuildMethodName() {
        return "build";
    }

    public String getCommandsMethodName() {
        return "commands";
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

    public String getClassName() {
        return className;
    }

    public ClassType getExtendsClass() {
        return extendsClass;
    }

    @Override
    public String toString() {
        return "ValidatorBuilderPrototypeElement{" +
                "parentVB=" + parentBean +
                ", targetPackageName='" + packageName + '\'' +
                ", targetClassName='" + className + '\'' +
                ", extendsClass=" + extendsClass +
                ", properties=" + properties +
                '}';
    }
}
