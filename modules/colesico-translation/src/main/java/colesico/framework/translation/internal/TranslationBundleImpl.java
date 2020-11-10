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

import colesico.framework.translation.TranslationBundle;
import colesico.framework.translation.assist.bundle.PropertyBundle;

import java.text.MessageFormat;
import java.util.List;

/**
 * Implementation based on underlying {@link PropertyBundle}
 */
public final class TranslationBundleImpl implements TranslationBundle {

    private final PropertyBundle bundle;

    public TranslationBundleImpl(PropertyBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String get(final String key, final String defaultVal, final Object... params) {
        final String translation = bundle.getString(key);

        if (translation == null) {
            return defaultVal;
        }

        if (params.length > 0) {
            return MessageFormat.format(translation, params);
        }

        return translation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(bundle.getBaseName()).append(":\n");
        List<String> keys = bundle.getKeys();
        for (String key : keys) {
            sb.append(key).append('=').append(bundle.getString(key)).append(";\n");
        }
        return sb.toString();
    }
}
