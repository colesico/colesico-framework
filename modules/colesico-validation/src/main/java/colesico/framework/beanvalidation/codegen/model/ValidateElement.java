package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.BeanValidate;

import javax.lang.model.type.TypeMirror;
import java.util.Locale;

/**
 * Element to be validated  (property field, bean field)
 *
 * @see colesico.framework.beanvalidation.Validate
 */
abstract public class ValidateElement {

    /**
     * Parent validator builder
     */
    protected ValidatorBuilderElement parentBuilder;

    protected final FieldElement originField;

    /**
     * Subject associated with this field
     */
    protected final String subject;

    /**
     * Field mapper command
     *
     * @see Validate#mapper()
     * @see BeanValidate#mapper()
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

    public final String fieldName() {
        if (originField != null) {
            return originField.name();
        }
        return null;
    }

    public final TypeMirror fieldType() {
        if (originField != null) {
            return originField.originType();
        }
        return null;
    }

    public final String fieldGetterName() {
        return "get" + StringUtils.firstCharToUpperCase(fieldName());
    }

    public final String filedReferenceName() {
        if (originField != null) {
            String kebabCase = StringUtils.toSeparatorNotation(originField.name(), '_');
            return kebabCase.toUpperCase(Locale.ROOT);
        }
        return null;
    }

    public ValidatorBuilderElement parentBuilder() {
        return parentBuilder;
    }

    public void setParentBuilder(ValidatorBuilderElement parentBuilder) {
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
