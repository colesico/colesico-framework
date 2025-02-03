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
 * Resources service  (resource name localizations, rewriting, etc.)
 */
public interface ResourceKit {

    /**
     * Returns localized resource name best matched with current profile
     *
     * @see colesico.framework.resource.l10n.L10nConfigPrototype#getObjectiveQualifiers(Profile)
     */
    String localize(String baseName);

    /**
     * Returns localized resource names ordered  by degree of matching with {@link ObjectiveQualifiers}
     * name[0] - best matching (same as {@link ResourceKit#localize(String)} )
     * name[N] - worst matching  (default resource)
     */
    String[] localizations(String baseName);

    /**
     *  Return objective qualifiers for current profile
     */
    ObjectiveQualifiers getObjectiveQualifiers();

    /**
     * Localize resource name and returns resource URLs
     */
    Enumeration<URL> getResourceURLs(String baseName);

    /**
     * Localize resource name and returns resource input stream
     */
    InputStream getResourceStream(String baseName);

}
