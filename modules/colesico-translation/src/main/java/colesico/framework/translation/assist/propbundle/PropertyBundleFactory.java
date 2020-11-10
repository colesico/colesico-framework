package colesico.framework.translation.assist.propbundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class PropertyBundleFactory {

    private static final Logger log = LoggerFactory.getLogger(PropertyBundleFactory.class);

    private final PropertyBundleCache cache;
    private final ResourceNameRewriter resourceNameRewriter;

    public PropertyBundleFactory(PropertyBundleCache cache, ResourceNameRewriter resourceNameRewriter) {
        this.cache = cache;
        this.resourceNameRewriter = resourceNameRewriter;
    }

    public PropertyBundle getBundle(String baseName, Locale locale) {
        if (StringUtils.isEmpty(baseName)) {
            throw new IllegalArgumentException("Base name is empty");
        } else if (locale == null) {
            throw new NullPointerException("Locale is null");
        }

        PropertyBundleCache.Key key = new PropertyBundleCache.Key(baseName, locale);
        PropertyBundle bundle = cache.get(key);
        if (bundle == null) {
            bundle = loadBundle(baseName, locale);
            cache.set(key, bundle);
        }

        return bundle;
    }

    protected PropertyBundle loadBundle(String baseName, Locale locale) {

        PropertyBundle bundle = null;

        for (Locale candidate : getCandidateLocales(locale)) {
            Properties properties = loadProperties(baseName, candidate);
            if (properties != null) {
                bundle = new PropertyBundle(bundle, baseName, candidate, properties);
            }
        }

        if (bundle == null) {
            throw new RuntimeException("Property bundle not found: " + baseName);
        }

        return bundle;
    }

    /**
     * Default locale candidates implementation
     */
    protected List<Locale> getCandidateLocales(Locale locale) {

        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        List<Locale> list = new LinkedList<>();

        list.add(Locale.ROOT);

        if (!language.isEmpty()) {
            list.add(new Locale(language, "", ""));
        }

        if (!country.isEmpty()) {
            list.add(new Locale(language, country, ""));
        }

        if (!variant.isEmpty()) {
            list.add(new Locale(language, country, variant));
        }

        return list;

    }

    protected String toResourceName(String baseName, Locale locale) {

        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        StringBuilder sb = new StringBuilder(baseName.length() + 20);
        sb.append(baseName.replace('.', '/'));

        if (!language.isEmpty()) {
            sb.append('_').append(language);
            if (!variant.isEmpty()) {
                sb.append('_').append(country).append('_').append(variant);
            } else if (!country.isEmpty()) {
                sb.append('_').append(country);
            }
        }

        return sb.append(".properties").toString();

    }

    protected Properties loadProperties(String baseName, Locale locale) {
        String resourceName = toResourceName(baseName, locale);
        if (resourceNameRewriter != null) {
            resourceName = resourceNameRewriter.rewrite(resourceName);
        }

        log.debug("Load properties from resource: {}", resourceName);

        Properties prop = new Properties();

        InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName);
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
