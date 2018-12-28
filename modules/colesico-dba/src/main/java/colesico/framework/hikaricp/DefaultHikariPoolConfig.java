package colesico.framework.hikaricp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Default HikariPool Config
 */

public class DefaultHikariPoolConfig extends HikariPoolConfig {

    protected final Logger log = LoggerFactory.getLogger(DefaultHikariPoolConfig.class);

    protected String getConfigFilePath() {
        return "META-INF/hikari.properties";
    }

    @Override
    public final Properties getProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(getConfigFilePath())) {
            props.load(resourceStream);
            return props;
        } catch (Exception ex) {
            log.error("Error loading hikari pool config file '" + getConfigFilePath() + "' : " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return "HikariPoolConfig{" + getConfigFilePath() + "}";
    }
}
