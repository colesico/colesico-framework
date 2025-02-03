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
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.ResourceNotFoundException;
import colesico.framework.resource.ResourcePrefixOptionsPrototype;
import colesico.framework.resource.internal.l10n.PathLocalizer;
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
public class ResourceKitImpl implements ResourceKit {

    protected final Logger log = LoggerFactory.getLogger(ResourceKit.class);

    private final PathLocalizer pathLocalizer;
    private final PrefixSubstitutor prefixSubstitutor;


    public ResourceKitImpl(PathLocalizer pathLocalizer, PrefixSubstitutor prefixSubstitutor) {
        this.pathLocalizer = pathLocalizer;
        this.prefixSubstitutor = prefixSubstitutor;
    }

    @Inject


    @Override
    public String rewrite(String path) {
        path = prefixSubstitutor.substitutePrefix(path, ResourcePrefixOptionsPrototype.Phase.BEFORE_LOCALIZE);
        path = pathLocalizer.localizePath(path);
        path = prefixSubstitutor.substitutePrefix(path, ResourcePrefixOptionsPrototype.Phase.AFTER_LOCALIZE);
        return path;
    }

    @Override
    public String[] localize(String path) {
        path = prefixSubstitutor.substitutePrefix(path, ResourcePrefixOptionsPrototype.Phase.BEFORE_LOCALIZE);
        String[] localizedPaths = pathLocalizer.localizedPaths(path);
        for (int i = 0; i < localizedPaths.length; i++) {
            localizedPaths[i] = prefixSubstitutor.substitutePrefix(localizedPaths[i], ResourcePrefixOptionsPrototype.Phase.AFTER_LOCALIZE);
        }
        return localizedPaths;
    }

    @Override
    public Enumeration<URL> getResourceURLs(String resourcePath) {
        try {
            resourcePath = rewrite(resourcePath);
            return getClassLoader().getResources(resourcePath);
        } catch (IOException e) {
            throw new ResourceException("Error reading resource URLs", e);
        }
    }

    @Override
    public InputStream getResourceStream(String resourcePath) {
        resourcePath = rewrite(resourcePath);
        InputStream in = getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new ResourceNotFoundException(resourcePath);
        }
        return in;
    }


    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
