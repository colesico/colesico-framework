package colesico.framework.dslvalidator.beanassist;

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
     * Validator builder  prototype package name
     */
    String packageName() default "";

    /**
     * Validator builder  prototype simple class name
     */
    String className() default "";

    Class<? extends ValidatorBuilder> extendsClass() default ValidatorBuilder.class;
}
