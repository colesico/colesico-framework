package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the method is not tele-parameter.
 * The value of this parameter will not be read/write via tele-api
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Inherited
@Documented
public @interface LocalParam {
}