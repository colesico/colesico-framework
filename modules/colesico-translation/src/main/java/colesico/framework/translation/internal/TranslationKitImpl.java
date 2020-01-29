/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.translation.internal;


import colesico.framework.ioc.StringKey;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.resource.ResourceKit;
import colesico.framework.translation.Bundle;
import colesico.framework.translation.Translatable;
import colesico.framework.translation.TranslationExceprion;
import colesico.framework.translation.TranslationKit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class TranslationKitImpl implements TranslationKit {

    public static final String BUNDLE_FILE_EXT = ".properties";
    public static final String SCOPE_KEY_PREFIX = "T9N/";

    protected final Logger log = LoggerFactory.getLogger(TranslationKit.class);

    protected final ResourceKit resourceKit;
    protected final ThreadScope threadScope;
    protected final BundleCache bundleCache;


    public TranslationKitImpl(ResourceKit resourceKit, ThreadScope threadScope) {
        this.resourceKit = resourceKit;
        this.threadScope = threadScope;
        this.bundleCache = new BundleCacheSoft(0.15);
    }

    @Override
    public Translatable getTranslatable(String basePath, String key) {
        return new TranslatableImpl(this, basePath, key);
    }

    @Override
    public Bundle getBundle(final String basePath) {
        // Check thread scope for bundle
        final StringKey<Bundle> scopeKey = new StringKey<>(SCOPE_KEY_PREFIX + basePath);
        Bundle bundle = threadScope.get(scopeKey);
        if (bundle != null) {
            return bundle;
        }

        bundle = getBundle(basePath, true);

        // Reference the bundle from thread scope to fast access in the same thread
        threadScope.put(scopeKey, bundle);

        return bundle;
    }

    /**
     * Returns new bundle from resource file or  from cache
     *
     * @return
     */
    public Bundle getBundle(String basePath, boolean localize) {
        String evaluatedPath = resourceKit.evaluate(basePath);
        String actualPath;
        boolean defaultBundle;
        if (localize) {
            String localizedPath = resourceKit.localize(evaluatedPath, ResourceKit.L10NMode.FILE);
            actualPath = resourceKit.rewrite(localizedPath);
            defaultBundle = StringUtils.equals(evaluatedPath, localizedPath);
        } else {
            actualPath = resourceKit.rewrite(evaluatedPath);
            defaultBundle = true;
        }

        final BundleCache.Key cacheKey = new BundleCache.Key(actualPath);

        Bundle bundle = bundleCache.get(cacheKey);

        if (bundle != null) {
            return bundle;
        }

        Properties translations = load(actualPath);
        bundle = defaultBundle ? new DefaultBundle(translations) : new ChainBundle(translations, evaluatedPath, this);
        bundleCache.set(cacheKey, bundle);
        return bundle;
    }


    /**
     * Loads translations from resource file.
     *
     * @param actualPath
     * @return
     */
    protected Properties load(final String actualPath) {

        String fullPath = actualPath + BUNDLE_FILE_EXT;
        log.debug("Loading translations from: " + fullPath);

        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (in == null) {
            String errMsg = "Translations file not found:" + fullPath;
            log.error(errMsg);
            throw new TranslationExceprion(errMsg);
        }
        try (InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);) {
            prop.load(isr);
            return prop;
        } catch (Exception ex) {
            String errMsg = MessageFormat.format("Error loading translations file: {0}; Cause message: {1}", fullPath, ExceptionUtils.getRootCauseMessage(ex));
            log.error(errMsg);
            throw new TranslationExceprion(errMsg, ex);
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                String errMsg = MessageFormat.format("Error closing translations file: {0}; Cause message: {1}", fullPath, ExceptionUtils.getRootCauseMessage(ex));
                log.error(errMsg);
                throw new TranslationExceprion(errMsg, ex);
            }
        }
    }

}
