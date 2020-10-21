package colesico.framework.beanvalidator.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator builder (builder prototype) element
 */
public class ValidatorBuilderElement {

    /**
     * Parent validated bean element
     */
    private ValidatedBeanElement parentBean;

    /**
     * Validator builder base package name
     */
    private final String targetPackageName;

    /**
     * Validator builder base class name
     */
    private final String targetClassName;

    private final ClassType extendsClass;

    private final List<ValidatedPropertyElement> properties = new ArrayList<>();

    public ValidatorBuilderElement(String targetPackageName, String targetClassName, ClassType extendsClass) {
        this.targetPackageName = targetPackageName;
        this.targetClassName = targetClassName;
        this.extendsClass = extendsClass;
    }

    public void addProperty(ValidatedPropertyElement property) {
        properties.add(property);
        property.setParentBuilder(this);
    }

    public String getBuildMethodName(){
        return "build";
    }

    public String getCommandsMethodName(){
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

    public String getTargetPackageName() {
        return targetPackageName;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public ClassType getExtendsClass() {
        return extendsClass;
    }

    @Override
    public String toString() {
        return "ValidatorBuilderPrototypeElement{" +
                "parentVB=" + parentBean +
                ", targetPackageName='" + targetPackageName + '\'' +
                ", targetClassName='" + targetClassName + '\'' +
                ", extendsClass=" + extendsClass +
                ", properties=" + properties +
                '}';
    }
}
