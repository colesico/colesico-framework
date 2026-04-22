package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the value of the parameter is assigned from a field of the
 * batch object, which is read from the data port as a single object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
public @interface BatchField {

    /**
     * Default batch name
     */
    String DEFAULT_BATCH = "request";

    /**
     * Batch class field name
     */
    String value() default "";

    /**
     * Batch name
     */
    String batch() default DEFAULT_BATCH;
}
