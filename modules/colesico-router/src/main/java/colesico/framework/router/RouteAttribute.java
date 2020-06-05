package colesico.framework.router;

import java.lang.annotation.*;

/**
 * Declare any specific attribute associated with route action method.
 * This attributes can be used for example in http server to tune each request handling.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
@Repeatable(RouteAttributes.class)
public @interface RouteAttribute {
    /**
     * Attribute name
     */
    String name();

    /**
     * Attribute value
     */
    String value();
}
