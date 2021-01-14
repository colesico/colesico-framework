package colesico.framework.introspection;

import java.lang.annotation.*;

/**
 * Indicates that an {@link MetaInterface} implementation  should be produced  for the given type
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Introspected {

    /**
     * Alternative classes that should actually be registered for introspection instead of the current class.
     */
    Class<?>[] targets() default {};
}
