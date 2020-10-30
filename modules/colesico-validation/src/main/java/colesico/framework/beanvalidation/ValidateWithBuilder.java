package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Validate this property as a bean with validation form another validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface ValidateWithBuilder {

    /**
     * Validator builder class full name.
     * If not specified will be used a builder prototype class name.
     */
    String builderClass() default "";

    /**
     * Property value may be null.
     * If false - property value required.
     */
    boolean optional() default true;
}
