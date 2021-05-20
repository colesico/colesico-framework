package colesico.framework.restlet.teleapi.jsonrequest;

import java.lang.annotation.*;

/**
 * Specifies read parameter from body json attribute
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Inherited
@Documented
public @interface JsonField {
}
