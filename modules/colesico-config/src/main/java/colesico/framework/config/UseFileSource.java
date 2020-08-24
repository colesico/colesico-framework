package colesico.framework.config;

import java.lang.annotation.*;

/**
 * @UseSource extension to use for file based config sources.
 * It is shortcut for @UseSource and @SourceOption(name = FILE_OPTION, value = "...")...
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface UseFileSource {

    String FILE_OPTION = "file";
    String DIRECTORY_OPTION = "directory";
    String CLASSPATH_OPTION = "classpath";

    /**
     * Config source type. PropertiesSource supported out-of the-box.
     *
     * @return
     * @see PropertiesSource
     */
    Class<? extends ConfigSource> type() default ConfigSource.class;

    /**
     * Assumes that values for all fields are assigned from config source.
     * If false - the  @{@link FromSource} annotation should be used to assign the value to field.
     *
     * @return
     * @see FromSource
     */
    boolean bindAll() default true;

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
