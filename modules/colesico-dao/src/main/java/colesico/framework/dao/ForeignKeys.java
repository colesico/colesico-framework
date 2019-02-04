package colesico.framework.dao;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface ForeignKeys {
    ForeignKey[] value();
}
