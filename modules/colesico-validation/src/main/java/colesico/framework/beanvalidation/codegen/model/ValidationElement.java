package colesico.framework.beanvalidation.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.FieldElement;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.type.TypeMirror;

/**
 * @see colesico.framework.beanvalidation.Validate
 */
abstract public class ValidationElement {

    /**
     * Parent validator builder element
     */
    protected ValidatorBuilderElement parentBuilder;

    protected final FieldElement originField;

    /**
     * Subject associated with this field
     */
    protected final String subject;

    public ValidationElement(FieldElement originField, String subject) {
        this.originField = originField;
        this.subject = subject;
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


    public ValidatorBuilderElement getParentBuilder() {
        return parentBuilder;
    }

    public void setParentBuilder(ValidatorBuilderElement parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public String getSubject() {
        return subject;
    }
}
