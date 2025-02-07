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

    public static final String VALIDATOR_BUILDER_PROTOTYPE_SUFFIX = "Validation";


    /**
     * Parent bean element (annotated with {@link ValidatorBuilder})
     */
    private BeanElement parentBean;

    /**
     * Validator builder prototype  name
     *
     * @see ValidatorBuilder#name()
     */
    private final String name;

    /**
     * Validator builder prototype package name
     */
    private final String packageName;

    private final ClassType superclass;

    /**
     * @see ValidatorBuilder#command()
     */
    private final String command;

    private final List<ValidationElement> validations = new ArrayList<>();

    public ValidatorBuilderElement(String name, String packageName, ClassType superclass, String command) {
        this.name = name;
        this.packageName = packageName;
        this.superclass = superclass;
        this.command = command;
    }

    public void addValidation(ValidationElement validation) {
        validations.add(validation);
        validation.setParentBuilder(this);
    }

    /**
     * Validator builder class simple name
     */
    public String getBuilderClassSimpleName() {
        return parentBean.getOriginType().asClassElement().getSimpleName()
                + StrUtils.firstCharToUpperCase(name)
                + VALIDATOR_BUILDER_PROTOTYPE_SUFFIX;
    }

    public String getBuilderClassName() {
        return packageName + "." + getBuilderClassSimpleName();
    }

    public List<ValidationElement> getValidations() {
        return validations;
    }

    public BeanElement getParentBean() {
        return parentBean;
    }

    public void setParentBean(BeanElement parentBean) {
        this.parentBean = parentBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public ClassType getSuperclass() {
        return superclass;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "ValidatorBuilderElement{" +
                "parentBean=" + parentBean +
                ", name='" + name + '\'' +
                ", command=" + command +
                ", packageName='" + packageName + '\'' +
                ", superclass=" + superclass +
                ", properties=" + validations +
                '}';
    }
}
