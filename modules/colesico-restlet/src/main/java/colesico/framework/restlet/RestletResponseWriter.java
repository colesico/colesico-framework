package colesico.framework.restlet;

import java.lang.annotation.*;


/**
 * Specifies custom writer for actualResponse
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface RestletResponseWriter {
    Class<? extends RestletTeleWriter> value();
}
