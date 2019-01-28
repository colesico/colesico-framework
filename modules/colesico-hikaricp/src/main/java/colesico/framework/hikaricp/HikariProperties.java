package colesico.framework.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Properties file based HikariCP config
 */
public class HikariProperties extends HikariConfiguration {

    protected final Logger log = LoggerFactory.getLogger(HikariProperties.class);

    protected String getPropertiesFilePath() {
        return "META-INF/hikaricp.properties";
    }

    @Override
    public final HikariConfig getDataSourceConfig() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(getPropertiesFilePath())) {
            props.load(resourceStream);
            return new HikariConfig(props);
        } catch (Exception ex) {
            log.error("Error loading hikari connection pool config file '" + getPropertiesFilePath() + "' : " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return "HikariProperties{" + getPropertiesFilePath() + "}";
    }
}