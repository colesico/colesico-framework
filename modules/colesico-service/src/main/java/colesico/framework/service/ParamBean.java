package colesico.framework.service;


import java.lang.annotation.*;

/**
 * Indicates that the method parameter or field is not just simple parameter
 * but an aggregation of params. Each field of this aggregation bean is a separate parameter or
 * nested aggregation. Aggregation bean class must have no args constructor and setters for each field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Inherited
@Documented
public @interface ParamBean {
}
