package colesico.framework.beanvalidation.codegen.model;

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
     * Validator builder prototype package name
     */
    private final String packageName;

    /**
     * Validator builder prototype class name
     */
    private final String classSimpleName;

    private final ClassType extendsClass;

    private final List<ValidatedPropertyElement> properties = new ArrayList<>();

    public ValidatorBuilderElement(String packageName, String classSimpleName, ClassType extendsClass) {
        this.packageName = packageName;
        this.classSimpleName = classSimpleName;
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

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public ClassType getExtendsClass() {
        return extendsClass;
    }

    @Override
    public String toString() {
        return "ValidatorBuilderElement{" +
                "parentBean=" + parentBean +
                ", targetPackageName='" + packageName + '\'' +
                ", targetClassName='" + classSimpleName + '\'' +
                ", extendsClass=" + extendsClass +
                ", properties=" + properties +
                '}';
    }
}
