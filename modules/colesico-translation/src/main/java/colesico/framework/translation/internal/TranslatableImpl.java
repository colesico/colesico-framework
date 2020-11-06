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

import colesico.framework.translation.Translatable;
import colesico.framework.translation.TranslationKit;

public class TranslatableImpl implements Translatable {

    private final TranslationKit translationKit;
    private final String basePath;
    private final String key;

    public TranslatableImpl(TranslationKit translationKit, String basePath, String key) {
        this.translationKit = translationKit;
        this.basePath = basePath;
        this.key = key;
    }

    @Override
    public String translate(Object... params) {
        return translationKit.getBundle(basePath).get(key,key, params);
    }
}
