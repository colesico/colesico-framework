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
 * Resource rewriter/localizer
 * Perform resource name localization and rewriting based on rules
 * defined in config files and the current context (profile)
 */
public interface ResourceResolver {

    /**
     * Rewrite/localize resource name best matched with current profile
     *
     * @see colesico.framework.resource.l10n.L10nConfigPrototype#objectiveQualifiers(Profile)
     */
    String resolve(String baseName);

    /**
     * Returns rewrote/localized resource names ordered  by degree of matching with {@link ObjectiveQualifiers}
     * name[N] - best matching (same as {@link ResourceResolver#resolve(String)} )
     * name[0] - worst matching  (default resource)
     */
    String[] resolutions(String baseName);

    /**
     *  Return objective qualifiers for current profile
     */
    ObjectiveQualifiers objectiveQualifiers();

    /**
     * Resolve resource name and returns resource URLs
     */
    Enumeration<URL> resourceURLs(String baseName);

    /**
     * Resolve resource name and returns resource input stream
     */
    InputStream resourceStream(String baseName);

}
