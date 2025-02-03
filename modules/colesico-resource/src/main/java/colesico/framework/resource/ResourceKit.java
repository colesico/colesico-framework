/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.resource;

import colesico.framework.profile.Profile;
import colesico.framework.resource.l10n.ObjectiveQualifiers;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * Resources service  (path localization rewriting, etc.)
 */
public interface ResourceKit {

    /**
     * Returns localized path best matched with current profile
     *
     * @see colesico.framework.resource.l10n.L10nConfigPrototype#getObjectiveQualifiers(Profile)
     */
    String rewrite(String path);

    /**
     * Returns localized paths ordered  by degree of matching with {@link ObjectiveQualifiers}
     * path[0] - best matching (same as {@link ResourceKit#rewrite(String)} )
     * path[N] - worst matching  (default resource)
     */
    String[] localize(String path);

    /**
     * Rewrite resource path and returns resource URLs
     */
    Enumeration<URL> getResourceURLs(String resourcePath);

    /**
     * Rewrite resource path and returns resource input stream
     */
    InputStream getResourceStream(String resourcePath);

}
