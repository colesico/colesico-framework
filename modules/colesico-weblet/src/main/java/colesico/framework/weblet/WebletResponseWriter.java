package colesico.framework.weblet;

import java.lang.annotation.*;


/**
 * Specifies custom writer for tele-actualResponse
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface WebletResponseWriter {
    Class<? extends WebletTeleWriter> value();
}
