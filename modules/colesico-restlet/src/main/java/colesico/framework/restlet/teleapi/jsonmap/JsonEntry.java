package colesico.framework.restlet.teleapi.jsonmap;

import java.lang.annotation.*;

/**
 * Specifies read parameter from json map  by parameter name
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Inherited
@Documented
public @interface JsonEntry {
}
