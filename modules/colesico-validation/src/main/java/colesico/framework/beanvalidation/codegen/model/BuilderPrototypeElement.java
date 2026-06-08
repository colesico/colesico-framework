package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.beanvalidation.ValidatorBuilderPrototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Validator Builder Prototype element
 *
 * @see ValidatorBuilderPrototype
 */
public class BuilderPrototypeElement {

    public static final String VALIDATOR_BUILDER_PROTOTYPE_PREFIX = "";
    public static final String VALIDATOR_BUILDER_PROTOTYPE_SUFFIX = "ValidatorBuilder";

    /**
     * Parent bean element (annotated with {@link ValidatorBuilderPrototype})
     */
    private BeanElement parentBean;

    /**
     * Validator builder prototype name
     *
     * @see ValidatorBuilderPrototype#name()
     */
    private final String name;

    /**
     * Validator builder  prototype package name
     */
    private final String packageName;

    private final ClassType superclass;

    /**
     * Bean default root subject
     */
    private final String subject;

    /**
     * @see ValidatorBuilderPrototype#command()
     */
    private final String command;

    private final List<ValidateElement> validations = new ArrayList<>();

    public BuilderPrototypeElement(String name, String packageName, ClassType superclass, String subject, String command) {
        this.name = name;
        this.packageName = packageName;
        this.superclass = superclass;
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
        String nameSuffix;
        if (ValidatorBuilderPrototype.DEFAULT_BUILDER.equals(name)) {
            nameSuffix = "";
        } else {
            nameSuffix = StringUtils.firstCharToUpperCase(name);
        }
        return VALIDATOR_BUILDER_PROTOTYPE_PREFIX
                + parentBean.originType().asClassElement().simpleName()
                + nameSuffix
                + VALIDATOR_BUILDER_PROTOTYPE_SUFFIX;
    }

    public String builderClassName() {
        return packageName + "." + builderClassSimpleName();
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

    public String name() {
        return name;
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
                ", name='" + name + '\'' +
                ", command=" + command +
                ", packageName='" + packageName + '\'' +
                ", superclass=" + superclass +
                ", properties=" + validations +
                '}';
    }
}
