package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Validate this field as a bean with validation form another validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Repeatable(BeanValidates.class)
@Inherited
@Documented
public @interface BeanValidate {

    /**
     * Target validation builder that be used to validation
     */
    Class<? extends BeanValidatorBuilder> target() default BeanValidatorBuilder.class;

    /**
     * Parent validator builder this validation belongs to.
     * If not specified, the default builder will be used
     *
     * @see ValidatorBuilder
     */
    Class<? extends BeanValidatorBuilder> builder() default BeanValidatorBuilder.class;

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

}
