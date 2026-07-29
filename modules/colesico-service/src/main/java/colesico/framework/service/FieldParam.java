package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the value of the parameter is assigned from a field of the
 * request bean, which is read from the data port as a single object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface FieldParam {

    /**
     * Default request bean name
     */
    String DEFAULT_BEAN = "request";

    /**
     * Request bean field name
     */
    String value() default "";

    /**
     * Request bean name
     */
    String bean() default DEFAULT_BEAN;
}
