package colesico.framework.config;

import java.lang.annotation.*;

/**
 * Configuration source parameter alias.
 * It is shortcut for @SourceOption(name = FILE_OPTION, value = "...")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface FileSource {

    String FILE_OPTION = "file";
    String DIRECTORY_OPTION = "directory";
    String CLASSPATH_OPTION = "classpath";

    /**
     * Config file name
     */
    String file() default "";

    /**
     * Config file  directory
     */
    String directory() default "";

    /**
     * Config file classpath directory
     */
    String classpath() default "";
}
