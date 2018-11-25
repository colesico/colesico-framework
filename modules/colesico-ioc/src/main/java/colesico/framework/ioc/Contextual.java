package colesico.framework.ioc;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * Use this annotation to pass InjectionPoint to instance factory.
 * This annotation is used to mark the constructor parameter to pass the information about the class in which this parameter is injected.
 * This information is used while the parameter instance been created.
 * For example this annotation should be used to context dependent logger injection.
 *
 *
 * @see InjectionPoint
 */
@Qualifier
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Contextual {
}
