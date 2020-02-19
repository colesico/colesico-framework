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

package colesico.framework.profile;

import java.io.Serializable;
import java.util.Locale;

/**
 * Localization profile.
 * The specific implementation depends on the needs of the application and is implemented in the application.
 * Framework provides default implementation {@link DefaultProfile}
 */
public interface Profile extends Cloneable, Serializable {

    String GET_LOCALE_METHOD = "getLocale";
    String GET_QUALIFIERS_METHOD = "getQualifiers";

    /**
     * Returns client locale.
     *
     * @return
     */
    Locale getLocale();

    /**
     * Returns localization qualifier values
     */
    ObjectiveQualifiers getQualifiers();
}
