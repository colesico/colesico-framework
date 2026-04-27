package colesico.framework.translation.assist.propbundle;

import colesico.framework.resource.ResourceUtils;
import colesico.framework.resource.l10n.ObjectiveQualifiers;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;

@Singleton
public class PropertyBundleFactory {

    protected static final Logger log = LoggerFactory.getLogger(PropertyBundleFactory.class);

    protected final PropertyBundleCache cache;

    protected final ResourceUtils resourceUtils;

    public PropertyBundleFactory(PropertyBundleCache cache, ResourceUtils resourceUtils) {
        this.cache = cache;
        this.resourceUtils = resourceUtils;
    }

    @Inject
    public PropertyBundle getBundle(String baseName) {
        if (StringUtils.isEmpty(baseName)) {
            throw new IllegalArgumentException("Base name is empty");
        }
        ObjectiveQualifiers qualifiers = resourceUtils.objectiveQualifiers();
        PropertyBundleCache.Key key = new PropertyBundleCache.Key(baseName, qualifiers);
        PropertyBundle bundle = cache.get(key);
        if (bundle == null) {
            bundle = loadBundle(baseName);
            cache.set(key, bundle);
        }

        return bundle;
    }

    protected PropertyBundle loadBundle(String baseName) {

        PropertyBundle bundle = null;

        String[] localizedNames = resourceUtils.localizations(baseName);

        for (String localizedName : localizedNames) {
            Properties properties = loadProperties(localizedName + ".properties");
            if (properties != null) {
                bundle = new PropertyBundle(bundle, localizedName, properties);
            }
        }

        if (bundle == null) {
            throw new RuntimeException("Property bundle not found: " + baseName);
        }

        return bundle;
    }

    protected Properties loadProperties(String resourceName) {
        log.debug("Load properties from resource: {}", resourceName);

        Properties prop = new Properties();

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);

        if (in == null) {
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);) {
            prop.load(isr);
            return prop;
        } catch (Exception ex) {
            String errMsg = MessageFormat.format("Error loading properties file: {0}; Cause message: {1}", resourceName, ExceptionUtils.getRootCauseMessage(ex));
            log.error(errMsg);
            throw new RuntimeException(errMsg, ex);
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                String errMsg = MessageFormat.format("Error closing properties file: {0}; Cause message: {1}", resourceName, ExceptionUtils.getRootCauseMessage(ex));
                log.error(errMsg);
                throw new RuntimeException(errMsg, ex);
            }
        }
    }

}
