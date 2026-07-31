package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the value of the parameter is assigned from a field
 * of the param composition bean, that is read from the data port as a single object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface BeanField {

    /**
     * Default composition bean name
     */
    String DEFAULT_BEAN = "request";

    /**
     * Composition bean field name
     */
    String value() default "";

    /**
     * Composition bean name
     */
    String bean() default DEFAULT_BEAN;
}
