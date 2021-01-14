package colesico.framework.introspection;

import java.lang.annotation.*;

/**
 * Specifies the introspection  creation for given type
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Introspect {

    /**
     * Alternative classes that should actually be registered for introspection instead of the current class.
     */
    Class<?>[] targets() default {};
}
