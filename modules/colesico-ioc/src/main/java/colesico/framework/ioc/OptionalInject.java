package colesico.framework.ioc;

import javax.inject.Qualifier;
import java.lang.annotation.*;


/**
 * Specifies an optional injection.
 * If this annotation in specified on constructor parameter the parameter value may be null in case the
 * dependency is not found.
 */
@Qualifier
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalInject {
}
