package colesico.framework.ioc;

import javax.inject.Qualifier;
import java.lang.annotation.*;


/**
 * Indicates that the injection parameter is an IoC message.
 *
 * IoC messages are not retrieved statically from the IOC container
 * but are passed as a parameter to the instance factory
 */
@Qualifier
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Message {
}
