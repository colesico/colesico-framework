package colesico.framework.beanvalidation;

import colesico.framework.dslvalidator.builder.FlowControlBuilder;
import colesico.framework.dslvalidator.builder.ValidatorBuilder;

import java.lang.annotation.*;

/**
 * Declares the generating the prototype class for the validator builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(ValidatorBuilderPrototypes.class)
@Inherited
@Documented
public @interface ValidatorBuilderPrototype {

    /**
     * Validator builder prototype package name
     */
    String packageName() default "";

    /**
     * Validator builder prototype package name from specified class
     */
    Class<?> packageFromClass() default Class.class;

    /**
     * Validator builder  prototype simple class name
     */
    String className() default "";

    Class<? extends FlowControlBuilder> extendsClass() default ValidatorBuilder.class;
}
