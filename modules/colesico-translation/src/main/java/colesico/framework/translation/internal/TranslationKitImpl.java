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


import colesico.framework.ioc.key.StringKey;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.resource.ResourceKit;
import colesico.framework.translation.TranslationBundle;
import colesico.framework.translation.Translatable;
import colesico.framework.translation.TranslationExceprion;
import colesico.framework.translation.TranslationKit;
import colesico.framework.translation.assist.bundle.PropertyBundle;
import colesico.framework.translation.assist.bundle.PropertyBundleCacheSoft;
import colesico.framework.translation.assist.bundle.PropertyBundleFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import static java.util.ResourceBundle.Control.FORMAT_PROPERTIES;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class TranslationKitImpl implements TranslationKit {

    public static final String SCOPE_KEY_PREFIX = "T9N/";

    protected final Logger logger = LoggerFactory.getLogger(TranslationKit.class);

    protected final Provider<Locale> localeProv;
    protected final ThreadScope threadScope;
    protected final ResourceKit resourceKit;

    protected final PropertyBundleFactory propertyBundleFactory = new PropertyBundleFactory(new PropertyBundleCacheSoft(0.3));

    public TranslationKitImpl(Provider<Locale> localeProv, ThreadScope threadScope, ResourceKit resourceKit) {
        this.localeProv = localeProv;
        this.threadScope = threadScope;
        this.resourceKit = resourceKit;
    }

    @Override
    public Translatable getTranslatable(String baseName, String key) {
        return new TranslatableImpl(this, baseName, key);
    }

    @Override
    public TranslationBundle getBundle(String baseName) {

        // Check thread scope for bundle
        final StringKey<TranslationBundle> scopeKey = new StringKey<>(SCOPE_KEY_PREFIX + baseName);
        TranslationBundle translationBundle = threadScope.get(scopeKey);
        if (translationBundle != null) {
            return translationBundle;
        }

        baseName = resourceKit.rewrite(baseName);
        PropertyBundle propertyBundle = propertyBundleFactory.getBundle(baseName, localeProv.get());

        translationBundle = new TranslationBundleImpl(propertyBundle);

        // Reference the bundle from thread scope to fast access in the same thread
        threadScope.put(scopeKey, translationBundle);

        return translationBundle;
    }


}
