package colesico.framework.telehttp;

import java.lang.annotation.*;


/**
 * Specifies custom tele writer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface UseWriter {
    Class<? extends HttpWriter> value();
}
