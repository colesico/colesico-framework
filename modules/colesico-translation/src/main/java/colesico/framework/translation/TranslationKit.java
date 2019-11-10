/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.translation;

/**
 * Text translation service.
 *
 * @author Vladlen Larionov
 */
public interface TranslationKit {

    String GET_BUNDLE_METHOD = "getBundle";

    /**
     * Returns appropriate translations bundle for the current locale.
     * This method should cache the bundle in the thread scope to allow fast multiple access within the thread.
     *
     * @param basePath - resource path to *.properties file without extension and  localization qualifiers.
     *                 This path is an analogue of baseName for ResourceBundle.getBundle(...)
     * @return
     */
    Bundle getBundle(String basePath);

    Translatable getTranslatable(String basePath, String key);

    /**
     * Base path helper
     *
     * @param clazz
     * @return
     */
    static String toBasePath(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    static String toBasePath(Package pkg) {
        return pkg.getName().replace('.', '/');
    }
}
