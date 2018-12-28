package colesico.framework.ioc;

import javax.inject.Qualifier;
import java.lang.annotation.*;


/**
 * This annotation should be used in case of cyclic dependencies. Inline injection
 * has slightly lower performance and does not participate in circular dependencies checking.
 * The  dependencies of the instance factory will be obtained from the IoC container
 * at the instance production and not at the factory activation.
 */
@Qualifier
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InlineInject {
}
