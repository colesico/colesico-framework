package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Declares the generating the bean validator builder prototype
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(ValidatorBuilderPrototypes.class)
@Inherited
@Documented
public @interface ValidatorBuilderPrototype {

    /**
     * Bean validator builder prototype package name
     */
    String packageName() default "";

    /**
     * Bean validator builder prototype package name from specified class
     */
    Class<?> packageClass() default Class.class;

    /**
     * Bean validator builder prototype simple class name.
     * If not specified the prototype class name will be constructed in this way:
     * '[validated property name] +'ValidatorBuilderPrototype':
     */
    String className() default "";

    Class<? extends BeanValidatorBuilder> extendsClass() default BeanValidatorBuilder.class;
}
