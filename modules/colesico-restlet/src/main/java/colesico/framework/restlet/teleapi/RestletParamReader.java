package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.ParamReaderSpecifier;

import java.lang.annotation.*;


/**
 * Specifies custom reader for tele-param
 */
@Retention(RetentionPolicy.RUNTIME)
// ElementType.FIELD - for object reader. not implemented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Documented
@ParamReaderSpecifier
public @interface RestletParamReader {
    Class<? extends RestletTeleReader> value();
}
