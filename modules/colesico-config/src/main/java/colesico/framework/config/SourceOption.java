package colesico.framework.config;

import java.lang.annotation.*;

/**
 * Configuration source parameter (configuration source connection param, self configuration , e.t.c)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@Repeatable(SourceOptions.class)
public @interface SourceOption {

    /**
     * Parameter name
     */
    String name();

    /**
     * Parameter value
     */
    String value();
}
