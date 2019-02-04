package colesico.framework.dao;

import java.lang.annotation.*;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtraColumns {

    /**
     * Table extra columns that not bound to class fields
     *
     * @return
     */
    Column[] value();
}
