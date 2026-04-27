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
package colesico.framework.resource.internal;

import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceUtils;
import colesico.framework.resource.ResourceNotFoundException;
import colesico.framework.resource.ResourcePrefixOptionsPrototype;
import colesico.framework.resource.internal.l10n.Localizer;
import colesico.framework.resource.l10n.ObjectiveQualifiers;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class ResourceUtilsImpl implements ResourceUtils {

    protected final Logger log = LoggerFactory.getLogger(ResourceUtils.class);

    private final Localizer localizer;
    private final PrefixSubstitutor prefixSubstitutor;

    @Inject
    public ResourceUtilsImpl(Localizer localizer, PrefixSubstitutor prefixSubstitutor) {
        this.localizer = localizer;
        this.prefixSubstitutor = prefixSubstitutor;
    }

    @Override
    public String localize(String baseName) {
        baseName = prefixSubstitutor.substitutePrefix(baseName, ResourcePrefixOptionsPrototype.Phase.BEFORE_LOCALIZE);
        baseName = localizer.localize(baseName);
        baseName = prefixSubstitutor.substitutePrefix(baseName, ResourcePrefixOptionsPrototype.Phase.AFTER_LOCALIZE);
        return baseName;
    }

    @Override
    public String[] localizations(String baseName) {
        baseName = prefixSubstitutor.substitutePrefix(baseName, ResourcePrefixOptionsPrototype.Phase.BEFORE_LOCALIZE);
        String[] localizations = localizer.localizations(baseName);
        for (int i = 0; i < localizations.length; i++) {
            localizations[i] = prefixSubstitutor.substitutePrefix(localizations[i], ResourcePrefixOptionsPrototype.Phase.AFTER_LOCALIZE);
        }
        return localizations;
    }

    @Override
    public ObjectiveQualifiers objectiveQualifiers() {
        return localizer.objectiveQualifiers();
    }

    @Override
    public Enumeration<URL> resourceURLs(String baseName) {
        try {
            String resourceName = localize(baseName);
            return classLoader().getResources(resourceName);
        } catch (IOException e) {
            throw new ResourceException("Error reading resource URLs", e);
        }
    }

    @Override
    public InputStream resourceStream(String baseName) {
        String resourceName = localize(baseName);
        InputStream in = classLoader().getResourceAsStream(resourceName);
        if (in == null) {
            throw new ResourceNotFoundException(resourceName);
        }
        return in;
    }


    private ClassLoader classLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
