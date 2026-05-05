package colesico.framework.service;

import colesico.framework.ioc.production.Classed;

import java.lang.annotation.*;

/**
 * Parameter will receive value from the IoC container (like constructor params injection)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Inherited
@Documented
public @interface InjectParam {

    /**
     * Use @Named injection
     *
     * @see jakarta.inject.Named
     */
    String named() default "";

    /**
     * Use @Classed annotation
     *
     * @see Classed
     */
    Class<?> classed() default Class.class;
}
