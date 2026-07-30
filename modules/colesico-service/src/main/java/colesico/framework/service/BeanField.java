package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the value of the parameter is assigned from a field of the
 * bean, that is read from the data port as a single object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface BeanField {

    /**
     * Default bean name
     */
    String DEFAULT_BEAN = "request";

    /**
     * Bean field name
     */
    String value() default "";

    /**
     * Bean name
     */
    String bean() default DEFAULT_BEAN;
}
