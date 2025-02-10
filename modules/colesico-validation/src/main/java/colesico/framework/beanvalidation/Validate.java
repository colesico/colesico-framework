package colesico.framework.beanvalidation;

import java.lang.annotation.*;

import static colesico.framework.beanvalidation.ValidatorBuilder.DEFAULT_BUILDER;

/**
 * Specifies that the bean property has to be validated.
 * <p>
 * In this case, the code generator creates a stub of the validation builder method for this property
 * and generates a code for including the validation of this field in the validation process.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Validate {

    /**
     * Validation subject overriding.
     * By default, subject is a name of validated property.
     */
    String subject() default "";

    /**
     * If true - validation method will be generated with the verifier signature.
     * Use this flag to  implement a  value direct check, rather than supply a validation command.
     */
    boolean verifier() default false;

    /**
     * Command to map field value to validation context
     *
     * @see colesico.framework.dslvalidator.builder.ValidationFlowBuilder
     */
    String command() default "map";

    /**
     * Validator builders names this validation belongs to.
     *
     * @see ValidatorBuilder#name()
     */
    String[] builders() default {DEFAULT_BUILDER};
}
