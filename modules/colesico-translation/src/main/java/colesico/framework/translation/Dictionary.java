package colesico.framework.translation;

import java.lang.annotation.*;

/**
 * Annotation processor process interfaces annotated with this annotation and
 * generate implementation of message methods that perform the translation.
 * In addition the [translation].properties files will be created for each locale if the @Translation annotations is present
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface Dictionary {

    /**
     * Resource path to dictionary *.properties file without extension.
     */
    String basePath() default "";
}
