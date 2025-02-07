package colesico.framework.beanvalidation;

import colesico.framework.dslvalidator.Iterator;
import colesico.framework.dslvalidator.command.MandatoryGroup;

import java.lang.annotation.*;

/**
 * Declares bean validator builder for given bean.
 * A bean validator builder prototype will be generated that should be extended
 * to implement the validation of the bean fields.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(ValidatorBuilders.class)
@Inherited
@Documented
public @interface ValidatorBuilder {

    /**
     * For the bean to be validated, multiple validator builders can be generated.
     * Each such builder has its own name. Different validation builders may validate different  fields of one bean.
     * This can be done by specifying the validator builder name in the annotations {@link Validate}, {@link ValidateBean}
     * Builder name can contain only alphanumeric char since it will be a validation builder class name suffix
     */
    String name() default "";

    /**
     * Validation sequence to be used to validate bean fields
     */
    Class<? extends Iterator> sequence() default MandatoryGroup.class;

    /**
     * Bean validator builder package name.
     * Package name obtaining order:
     * - packageName
     * - packageOf
     * - superclass
     * - validated bean
     */
    String packageName() default "";

    /**
     * Bean validator builder prototype package name from specified class
     */
    Class<?> packageOf() default Class.class;

    /**
     * Validator builder superclass to be extended by generated builder prototype
     */
    Class<? extends BeanValidatorBuilder> superclass() default BeanValidatorBuilder.class;

}
