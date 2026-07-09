package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Declares a bean validator builder for the given bean.
 * A bean validator builder will be generated to implement
 * the entire validation process for the bean's fields.
 * The generated validation builder always extends the provided
 * abstract super validation builder ({@link #value()}), which implements the field validation method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(ValidatorBuilders.class)
@Inherited
@Documented
public @interface ValidatorBuilder {

    /**
     * Superclass to be extended by generated validator builder
     */
    Class<? extends BeanValidatorBuilder> superclass() default BeanValidatorBuilder.class;

    /**
     * Alias for {@link #superclass()}
     */
    Class<? extends BeanValidatorBuilder> value() default BeanValidatorBuilder.class;

    /**
     * For the bean to be validated, multiple validator builders can be generated.
     * Each such builder has its own name. Different validation builders may validate different  fields of one bean.
     * This can be done by specifying the validator builder name in the annotations {@link Validate}, {@link BeanValidate}
     * Builder name can contain only alphanumeric char since it will be a validation builder class name suffix
     */
    boolean isDefault() default true;

    /**
     * Bean validation root subject
     */
    String subject() default "";

    /**
     * Validation root command to be used to iterate fields
     * possible values = series, chain, optional, mandatory, etc.
     *
     * @see colesico.framework.dslvalidator.builder.ValidationFlowBuilder
     */
    String command() default "mandatory";

    /**
     * Bean validator builder package name.
     * Package name obtaining order:
     * - {@link #packageName()}
     * - {@link #value()}
     */
    String packageName() default "";

}
