package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Declares bean validator builder prototype
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(ValidatorBuilders.class)
@Inherited
@Documented
public @interface ValidatorBuilder {

    /**
     * Bean validator builder prototype package name
     */
    String packageName() default "";

    /**
     * Bean validator builder prototype package name from specified class
     */
    Class<?> packageClass() default Class.class;

    /**
     * Bean validator builder prototype class simple name.
     * If not specified the prototype class name will be constructed in this way:
     * '[validated bean class name] +'ValidatorBuilderPrototype':
     */
    String classSimpleName() default "";

    Class<? extends BeanValidatorBuilder> extendsClass() default BeanValidatorBuilder.class;

}
