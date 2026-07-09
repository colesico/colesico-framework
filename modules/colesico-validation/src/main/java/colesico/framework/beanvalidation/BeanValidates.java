package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Declares the generating the prototype class for the validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface BeanValidates {
    BeanValidate[] value();
}
