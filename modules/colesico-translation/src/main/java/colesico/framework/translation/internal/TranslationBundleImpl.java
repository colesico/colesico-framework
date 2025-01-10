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

import colesico.framework.translation.TextFormatter;
import colesico.framework.translation.TranslationBundle;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Implementation based on underlying {@link ResourceBundle}
 */
public final class TranslationBundleImpl implements TranslationBundle {

    private final ResourceBundle bundle;
    private final TextFormatter formatter;

    public TranslationBundleImpl(ResourceBundle bundle, TextFormatter formatter) {
        this.bundle = bundle;
        this.formatter = formatter;
    }

    @Override
    public String get(final String key, final String defaultVal, final Object... params) {
        final String translation = bundle.getString(key);

        if (translation == null) {
            return defaultVal;
        }

        if (params.length > 0) {
            return formatter.format(translation, params);
        }

        return translation;
    }

    @Override
    public String toString() {
        String baseName = bundle.getBaseBundleName();
        StringBuilder sb = new StringBuilder(baseName == null ? "?" : baseName).append(":\n");
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            sb.append(key).append('=').append(bundle.getString(key)).append(";\n");
        }
        return sb.toString();
    }
}
