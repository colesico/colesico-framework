package colesico.framework.translation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface TranslationKey {
    String value();
}
