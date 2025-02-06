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
