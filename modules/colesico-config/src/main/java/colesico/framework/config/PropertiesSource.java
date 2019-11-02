package colesico.framework.config;

import colesico.framework.assist.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

/**
 * Properties file based configuration source.
 * Lookup properties file in external directory specified by "directory" config param.
 * If not found trying to read file from classpath specified by "classpath" parameter.
 *
 * @see UseSource
 * @see SourceValue
 */
@Singleton
public class PropertiesSource implements ConfigSource {

    /**
     * Properties file  directory
     * Default: ./config
     */
    public static final String DIRECTORY = "directory";

    /**
     * Properties file classpath
     * Default: META-INF
     */
    public static final String CLASSPATH = "classpath";

    /**
     * Properties file name.
     * Default: application.properties
     */
    public static final String FILE = "fileName";

    /**
     * Properties name prefix
     */
    public static final String PREFIX = "prefix";

    private static final Logger logger = LoggerFactory.getLogger(PropertiesSource.class);

    @Override
    public Connection connect(Map<String, String> params) {
        final String prefix = params.get(PREFIX);
        final String fileName = params.getOrDefault(FILE, "application.properties");
        final String directory = params.getOrDefault(DIRECTORY, "./config");
        String fullPath = StrUtils.concatPath(directory, fileName, "/");

        final Properties props = new Properties();
        final File directoryFile = new File(fullPath);
        if (directoryFile.exists()) {
            logger.debug("Read configuration from file: "+fullPath);
            try (FileInputStream is = new FileInputStream(fullPath)) {
                props.load(is);
                return new ConnectionImpl(props, prefix);
            } catch (Exception e) {
                String errorMsg = "Error reading config from file: " + fullPath;
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg, e);
            }
        }


        final String classpath = params.getOrDefault(CLASSPATH, "META-INF");
        fullPath = StrUtils.concatPath(classpath, fileName, "/");

        try (InputStream is = getClassLoader().getResourceAsStream(fullPath)) {
            props.load(is);
            return new ConnectionImpl(props, prefix);
        } catch (Exception e) {
            String errorMsg = "Error reading config from from resource: " + fullPath;
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

    protected <T> T convertFromString(Type valueType, String value) {
        throw new RuntimeException("Unsupported type: " + valueType);
    }

    private class ConnectionImpl implements Connection {

        private final Properties properties;
        private final String preffix;

        public ConnectionImpl(Properties properties, String preffix) {
            this.properties = properties;
            this.preffix = preffix;
        }

        @Override
        public <T> T getValue(Type valueType, String queryText, T defaultValue) {
            String key = (preffix != null ? preffix + '.' : "") + queryText;
            String value = properties.getProperty(key);
            if (value == null) {
                return defaultValue;
            }
            if (valueType == String.class) {
                return (T) value;
            }
            if (valueType == Integer.class) {
                return (T) Integer.valueOf(value);
            }
            if (valueType == Boolean.class) {
                return (T) Boolean.valueOf(value);
            }
            if (valueType == Long.class) {
                return (T) Long.valueOf(value);
            }
            if (valueType == Double.class) {
                return (T) Double.valueOf(value);
            }
            if (valueType == Short.class) {
                return (T) Short.valueOf(value);
            }
            return convertFromString(valueType, value);
        }

        @Override
        public void close() {

        }
    }

}
