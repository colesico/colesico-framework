package colesico.framework.translation;

import java.lang.annotation.*;

/**
 * Should be applied to annotation that acts as a text translation for specified locale
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
@Documented
public @interface Translation {
    String value();
}
