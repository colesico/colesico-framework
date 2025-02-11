package colesico.framework.beanvalidation;

import java.lang.annotation.*;

/**
 * Specify injectable bean validation builder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface ValidatorBuilder {


}
