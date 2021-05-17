package colesico.framework.weblet.teleapi;

import colesico.framework.telehttp.ParamReaderSpecifier;

import java.lang.annotation.*;


/**
 * Specifies custom reader for tele-param
 */
@Retention(RetentionPolicy.RUNTIME)
// ElementType.FIELD - for object reader
@Target({ElementType.FIELD, ElementType.PARAMETER,ElementType.METHOD})
@Inherited
@Documented
@ParamReaderSpecifier
public @interface WebletParamReader {
    Class<? extends WebletTeleReader> value();
}
