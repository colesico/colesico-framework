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

/**
 * Is used to retrieve string translations.
 * Bundle should contains a translations only for the concrete locale
 */
public interface TranslationBundle {

    String GET_METHOD = "get";

    /**
     * Returns the string by its key or the default value if string was not found.
     * Also performs the parameter substitution with MessageFormat.format(...)
     */
    //TODO: specify forrmater via config?
    String get(String key, String defaultValue, Object... params);

}
