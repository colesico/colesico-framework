package colesico.framework.beanvalidation;

import colesico.framework.dslvalidator.builder.FlowControlBuilder;
import colesico.framework.dslvalidator.builder.ValidatorBuilder;

import java.lang.annotation.*;

/**
 * Validate this property as a bean with validation form another validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface ImportValidation {

    /**
     * Validation builder to be used to validate property value
     */
    Class<? extends ValidatorBuilder> builder();

    /**
     * Property value may be null.
     * If false - property value required.
     */
    boolean optional = true;
}
