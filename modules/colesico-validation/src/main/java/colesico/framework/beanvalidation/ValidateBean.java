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
     * Bean validation builder to be used to validate property value
     */
    String builder();

    /**
     * Property value may be null.
     * If false - property value required.
     */
    boolean optional() default true;
}
