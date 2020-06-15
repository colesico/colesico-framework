package colesico.framework.restlet.teleapi;

import java.lang.annotation.*;


/**
 * Specifies custom reader for tele-param
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Inherited
@Documented
public @interface RestletCustomReader {
    Class<? extends RestletTeleReader> value();
}
