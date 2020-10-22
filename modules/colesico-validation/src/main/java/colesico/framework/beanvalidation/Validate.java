package colesico.framework.beanvalidation;

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
     * Validation subject overriding.
     * By default subject is a name of validated property.
     */
    String subject() default "";
    /**
     * If true - validation method will be generated with the verifier signature.
     * Use this flag to implement a  value direct check, rather than supply a validation command.
     */
    boolean verify() default false;

    /**
     * Name of the validation method in the validation builder.
     * By default validation method name is constructed as follow: 'validate'|'verify' + [validated property name]
     */
    String methodName() default "";
}
