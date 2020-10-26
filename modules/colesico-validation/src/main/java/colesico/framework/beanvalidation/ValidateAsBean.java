package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Validate this property as a bean with validation form another validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface ValidateAsBean {

    /**
     * Property value may be null.
     * If false - property value required.
     */
    boolean optional() default true;
}
