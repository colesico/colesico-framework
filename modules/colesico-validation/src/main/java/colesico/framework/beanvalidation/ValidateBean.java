package colesico.framework.beanvalidation;

import java.lang.annotation.*;

import static colesico.framework.beanvalidation.ValidatorBuilderPrototype.DEFAULT_BUILDER;

/**
 * Validate this property as a bean with validation form another validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface ValidateBean {

    /**
     * Validation subject overriding.
     * By default, subject is a name of validated property.
     */
    String subject() default "";

    /**
     * Command to map field value to validation context
     *
     * @see colesico.framework.dslvalidator.builder.ValidationFlowBuilder
     */
    String mapper() default "map";

    /**
     * Validator builders names this validation belongs to.
     *
     * @see ValidatorBuilderPrototype#name()
     */
    String[] builders() default {DEFAULT_BUILDER};

    /**
     * Target validation builder name that be used
     */
    String targetBuilder() default DEFAULT_BUILDER;
}
