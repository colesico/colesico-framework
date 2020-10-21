package colesico.framework.beanvalidator;

import java.lang.annotation.*;

/**
 * Declares the generating the prototype class for the validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface ValidatorBuilderPrototypes {
    ValidatorBuilderPrototype[] value();
}
