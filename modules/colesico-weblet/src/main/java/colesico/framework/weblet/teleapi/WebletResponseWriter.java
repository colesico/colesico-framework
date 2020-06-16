package colesico.framework.weblet.teleapi;

import java.lang.annotation.*;


/**
 * Specifies custom writer for tele-param
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface WebletResponseWriter {
    Class<? extends WebletTeleWriter> value();
}
