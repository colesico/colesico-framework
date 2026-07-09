package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Specifies that the bean property has to be validated.
 * <p>
 * In this case, the code generator generates a code for including the validation of this field in the validation process.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
@Repeatable(Validates.class)
@Inherited
@Documented
public @interface Validate {

    /**
     * Parent validator builder this validation belongs to.
     * If not specified, the default builder will be used
     *
     * @see ValidatorBuilder
     */
    Class<? extends BeanValidatorBuilder> builder() default BeanValidatorBuilder.class;

    /**
     * Validation method name within builder
     * Default - as validated field name.
     */
    String method() default "";

    /**
     * Validation subject overriding.
     * By default, subject is a name of validated field.
     */
    String subject() default "";

    /**
     * If true - validation method has verifier signature.
     * Use this flag to  implement a  value direct check, rather than supply a validation command.
     */
    boolean verifier() default false;

    /**
     * Command to map field value to validation context
     *
     * @see colesico.framework.dslvalidator.builder.ValidationFlowBuilder
     */
    String mapper() default "map";

}
