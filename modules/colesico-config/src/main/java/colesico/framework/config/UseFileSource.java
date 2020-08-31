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
    String PREFIX_OPTION = "prefix";

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
     * @see FromSource
     */
    boolean bindAll() default true;

    /**
     * Value name default prefix.
     * Configuration variable name prefix which will be used  when retrieving a value from a configuration source.
     */
    String prefix() default "";

    /**
     * Config file name
     */
    String file() default "";

    /**
     * Config file directory
     */
    String directory() default "";

    /**
     * Config file classpath directory
     */
    String classpath() default "";
}
