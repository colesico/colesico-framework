package colesico.framework.translation.assist;

import colesico.framework.translation.Translation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Inherited
@Translation("en")
public @interface En {
    String value();
}
