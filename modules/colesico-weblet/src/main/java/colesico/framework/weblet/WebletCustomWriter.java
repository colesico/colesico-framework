package colesico.framework.weblet;

import java.lang.annotation.*;


/**
 * Specifies custom tele writer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface WebletCustomWriter {
    Class<? extends WebletWriter> value();
}
