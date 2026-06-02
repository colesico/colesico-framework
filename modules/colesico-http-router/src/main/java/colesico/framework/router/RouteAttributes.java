package colesico.framework.router;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface RouteAttributes {
    RouteAttribute[] value();
}
