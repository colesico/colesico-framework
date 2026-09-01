package colesico.framework.security.authentication;


import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Authentications {
    Authentication[] value();
}
