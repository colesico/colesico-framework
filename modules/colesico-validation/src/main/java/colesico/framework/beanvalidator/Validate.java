package colesico.framework.beanvalidator;

import java.lang.annotation.*;

/**
 * Specifies that the bean field has to be validated.
 *
 * In this case, the code generator creates a stub of the validation builder method for this field
 * and generates a code for including the validation of this field in the validation process.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Validate {

    /**
     * If true - validation method will be generated with the verifier signature.
     * Use this flag to implement a  value direct check, rather than supply a validation command.
     */
    boolean asVerifier() default false;
}
