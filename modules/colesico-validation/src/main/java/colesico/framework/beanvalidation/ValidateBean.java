package colesico.framework.beanvalidation;

import java.lang.annotation.*;

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
     * Validator builders names this validation belongs to.
     *
     * @see ValidatorBuilder#name()
     */
    String[] builders() default {};

    /**
     * Target validation builder name that be used
     */
    String targetBuilder() default "";
}
