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

package colesico.framework.translation;

abstract public class AbstractDictionary implements TranslationDictionary {

    public static final String TRANSLATE_OR_KEY_METHOD = "translateOrKey";
    public static final String TRANSLATION_KIT_FIELD = "translationKit";
    public static final String BASE_PATH_FIELD = "basePath";

    protected final String basePath;
    protected final TranslationKit translationKit;

    public AbstractDictionary(TranslationKit translationKit, String basePath) {
        this.basePath = basePath;
        this.translationKit = translationKit;
    }

    public final String getBasePath() {
        return basePath;
    }

    protected final String translateOrKey(final String key, Object... params) {
        return translationKit.getBundle(basePath).get(key, key, params);
    }

    @Override
    public final String translate(final String key, final String defaultValue, Object... params) {
        return translationKit.getBundle(basePath).get(key, defaultValue, params);
    }

    @Override
    public final TranslationBundle getBundle() {
        return translationKit.getBundle(basePath);
    }
}
