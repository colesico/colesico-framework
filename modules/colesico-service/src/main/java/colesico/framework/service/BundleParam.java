package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the value of the parameter is assigned from a field of the
 * bundle bean, that is read from the data port as a single object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface BundleParam {

    /**
     * Default bundle name
     */
    String DEFAULT_BUNDLE = "request";

    /**
     * Bundle field name
     */
    String value() default "";

    /**
     * Bundle name
     */
    String bundle() default DEFAULT_BUNDLE;
}
