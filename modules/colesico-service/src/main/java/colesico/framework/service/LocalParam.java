package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Specifies that the param should not be read from data port
 *
 * @see ParamsBean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface LocalParam {
}
