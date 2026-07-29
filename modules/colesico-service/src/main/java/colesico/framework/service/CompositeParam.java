package colesico.framework.service;


import java.lang.annotation.*;

/**
 * Indicates that the method parameter (or field) is not just simple parameter
 * but a composition of params. Each field of this bean is a separate parameter or
 * nested composition. Bean class must have no args constructor and setters for each field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Inherited
@Documented
public @interface CompositeParam {
}
