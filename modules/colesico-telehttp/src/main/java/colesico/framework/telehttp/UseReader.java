package colesico.framework.telehttp;

import java.lang.annotation.*;


/**
 * Specifies custom tele reader
 */
@Retention(RetentionPolicy.RUNTIME)
// ElementType.FIELD - for object reader
@Target({ElementType.FIELD, ElementType.PARAMETER,ElementType.METHOD})
@Inherited
@Documented
public @interface UseReader {
    Class<? extends HttpReader> value();
}
