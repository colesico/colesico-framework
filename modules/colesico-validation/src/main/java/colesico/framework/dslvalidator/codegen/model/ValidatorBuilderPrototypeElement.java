package colesico.framework.dslvalidator.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

public class ValidatorBuilderPrototypeElement {

    /**
     * Parent validated bean element
     */
    private ValidatedBeanElement parentVB;

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

    public ValidatorBuilderPrototypeElement(String targetPackageName, String targetClassName, ClassType extendsClass) {
        this.targetPackageName = targetPackageName;
        this.targetClassName = targetClassName;
        this.extendsClass = extendsClass;
    }

    public void addProperty(ValidatedPropertyElement property) {
        properties.add(property);
        property.setParentVBP(this);
    }

    public List<ValidatedPropertyElement> getProperties() {
        return properties;
    }

    public ValidatedBeanElement getParentVB() {
        return parentVB;
    }

    public void setParentVB(ValidatedBeanElement parentVB) {
        this.parentVB = parentVB;
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
}
