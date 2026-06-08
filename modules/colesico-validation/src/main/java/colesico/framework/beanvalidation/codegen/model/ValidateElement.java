package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidateBean;

import javax.lang.model.type.TypeMirror;

/**
 * Element to be validated  (property field, bean field)
 *
 * @see colesico.framework.beanvalidation.Validate
 */
abstract public class ValidateElement {

    /**
     * Parent validator builder element
     */
    protected BuilderPrototypeElement parentBuilder;

    protected final FieldElement originField;

    /**
     * Subject associated with this field
     */
    protected final String subject;

    /**
     * Field mapper command
     *
     * @see Validate#mapper()
     * @see ValidateBean#mapper()
     */
    protected final String mapper;

    public ValidateElement(FieldElement originField, String subject, String mapper) {
        this.originField = originField;
        this.subject = subject;
        this.mapper = mapper;
    }

    /**
     * Method which will be called to obtain field validation
     */
    abstract public String validationMethodName();

    public final String propertyName() {
        if (originField != null) {
            return originField.name();
        }
        return null;
    }

    public final TypeMirror propertyType() {
        if (originField != null) {
            return originField.originType();
        }
        return null;
    }

    public final String propertyGetterName() {
        return "get" + StringUtils.firstCharToUpperCase(propertyName());
    }

    public final String propertyReferenceName() {
        if (originField != null) {
            String kebabCase = StringUtils.toSeparatorNotation(originField.name(), '_');
            return StringUtils.toRootUpperCase(kebabCase);
        }
        return null;
    }

    public BuilderPrototypeElement parentBuilder() {
        return parentBuilder;
    }

    public void setParentBuilder(BuilderPrototypeElement parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

    public FieldElement originField() {
        return originField;
    }

    public String subject() {
        return subject;
    }

    public String mapper() {
        return mapper;
    }

}
