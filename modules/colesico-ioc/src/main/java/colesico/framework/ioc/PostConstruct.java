package colesico.framework.ioc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
@Inherited
public @interface PostConstruct {
}
