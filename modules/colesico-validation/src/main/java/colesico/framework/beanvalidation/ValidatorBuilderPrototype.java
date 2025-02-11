package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Declares bean validator builder prototype for given bean.
 * A bean validator builder prototype will be generated that should be extended
 * to implement the validation of the bean fields.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(ValidatorBuilderPrototypes.class)
@Inherited
@Documented
public @interface ValidatorBuilderPrototype {

    String DEFAULT_BUILDER ="default";

    /**
     * For the bean to be validated, multiple validator builder prototypes can be generated.
     * Each such builder prototype has its own name. Different validation builders may validate different  fields of one bean.
     * This can be done by specifying the validator builder name in the annotations {@link Validate}, {@link ValidateBean}
     * Builder name can contain only alphanumeric char since it will be a validation builder class name suffix
     */
    String name() default DEFAULT_BUILDER;

    /**
     * Validation root command to be used to iterate fields
     * possible values = series, chain, optional, mandatory, etc.
     *
     * @see colesico.framework.dslvalidator.builder.ValidationFlowBuilder
     */
    String command() default "mandatory";

    /**
     * Bean validator builder prototype package name.
     * Package name obtaining order:
     * - packageName
     * - superclass
     * - validated bean
     */
    String packageName() default "";

    /**
     *  Superclass to be extended by generated validator builder prototype
     */
    Class<? extends BeanValidatorBuilder> superclass() default BeanValidatorBuilder.class;

}
