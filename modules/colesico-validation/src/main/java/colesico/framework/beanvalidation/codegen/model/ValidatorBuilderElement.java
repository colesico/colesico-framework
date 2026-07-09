package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.beanvalidation.ValidatorBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Validator Builder element
 *
 * @see ValidatorBuilder
 */
public class ValidatorBuilderElement {

    /**
     * Parent bean element (annotated with {@link ValidatorBuilder})
     */
    private BeanElement parentBean;

    /**
     * Validator builder superclass ref
     *
     * @see ValidatorBuilder#value()
     */
    private final ClassType superclass;

    /**
     * @see ValidatorBuilder#isDefault()
     */
    private final boolean isDefault;

    /**
     * Validator builder package name
     */
    private final String packageName;

    /**
     * Bean  root subject
     */
    private final String subject;

    /**
     * Root command
     *
     * @see ValidatorBuilder#command()
     */
    private final String command;

    /**
     * Validators related to this builder
     */
    private final List<ValidateElement> validations = new ArrayList<>();

    public ValidatorBuilderElement(ClassType superclass, boolean isDefault, String packageName, String subject, String command) {
        this.superclass = superclass;
        this.isDefault = isDefault;
        this.packageName = packageName;
        this.subject = subject;
        this.command = command;
    }

    public void addValidation(ValidateElement validation) {
        validations.add(validation);
        validation.setParentBuilder(this);
    }

    /**
     * Validator builder class simple name
     */
    public String builderClassSimpleName() {
        return superclass.asClassElement().simpleName()+"Impl";
    }

    public String builderClassName() {
        return packageName + "." + builderClassSimpleName();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public List<ValidateElement> validations() {
        return validations;
    }

    public BeanElement parentBean() {
        return parentBean;
    }

    public void parentBean(BeanElement parentBean) {
        this.parentBean = parentBean;
    }

    public String packageName() {
        return packageName;
    }

    public ClassType superclass() {
        return superclass;
    }

    public String subject() {
        return subject;
    }

    public String command() {
        return command;
    }

    @Override
    public String toString() {
        return "ValidatorBuilderElement{" +
                "parentBean=" + parentBean +
                ", superclass=" + superclass +
                ", packageName='" + packageName + '\'' +
                ", subject='" + subject + '\'' +
                ", command='" + command + '\'' +
                ", validations=" + validations +
                '}';
    }
}
