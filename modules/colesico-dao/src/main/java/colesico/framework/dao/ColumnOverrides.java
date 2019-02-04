package colesico.framework.dao;

import java.lang.annotation.*;


/**
 * Column overrides
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnOverrides {

    /**
     * Table columns attributes overriding
     *
     * @return
     */
    Column[] value();
}
