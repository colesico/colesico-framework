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
package colesico.framework.resource;

import colesico.framework.profile.ProfileQualifiers;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * Resources service  (localization, rewriting, etc.)
 *
 * @author Vladlen Larionov
 */
public interface ResourceKit {

    /**
     * Returns rewritten resource path if the rewriting was defined via config.
     * It is possible to overwrite the path by a partial prefix match.
     * E.g for the rewriting  '/etc/srv'->'/foo'   path '/etc/srv/generator/x' will be rewritten to '/foo/generator/x'
     *
     * @return
     * @see ResourceOptionsPrototype
     */
    String rewrite(String resourcePath);

    /**
     * Localize path according to  current profile
     */
    String localize(String resourcePath, L10NMode mode);

    /**
     * Localize path according to  given qualifiers
     */
    String localize(String resourcePath, L10NMode mode, ProfileQualifiers qualfiers);

    /**
     * Evaluate  path with variables to concrete path
     */
    String evaluate(String resourcePath);

    Enumeration<URL> getURLs(String resourcePath);

    InputStream getStream(String resourcePath);

    /**
     * Resource localization mode
     */
    enum L10NMode {

        /**
         * Doesn't use localization
         */
        NONE,

        /**
         * Localize as file, e.g. path/file.ext -> path/file_ru_RU.ext
         */
        FILE,

        /**
         * Localize as directory, e.g. path/file.ext -> path_en_UK/file.ext
         */
        DIR
    }

}
