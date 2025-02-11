package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.Validate;
import colesico.framework.beanvalidation.ValidateBean;
import org.apache.commons.lang3.StringUtils;

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
    protected ValidatorBuilderPrototypeElement parentBuilder;

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
    abstract public String getValidationMethodName();

    public final String getPropertyName() {
        if (originField != null) {
            return originField.getName();
        }
        return null;
    }

    public final TypeMirror getPropertyType() {
        if (originField != null) {
            return originField.getOriginType();
        }
        return null;
    }

    public final String getPropertyGetterName() {
        return "get" + StrUtils.firstCharToUpperCase(getPropertyName());
    }

    public final String getPropertyReferenceName() {
        if (originField != null) {
            String kebabCase = StrUtils.toSeparatorNotation(originField.getName(), '_');
            return StringUtils.toRootUpperCase(kebabCase);
        }
        return null;
    }

    public ValidatorBuilderPrototypeElement getParentBuilder() {
        return parentBuilder;
    }

    public void setParentBuilder(ValidatorBuilderPrototypeElement parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public String getSubject() {
        return subject;
    }

    public String getMapper() {
        return mapper;
    }

}
