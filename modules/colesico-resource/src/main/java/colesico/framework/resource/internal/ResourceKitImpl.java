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
package colesico.framework.resource.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.profile.ObjectiveQualifiers;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.ResourceNotFoundException;
import colesico.framework.resource.ResourceOptionsPrototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class ResourceKitImpl implements ResourceKit {

    protected final Logger log = LoggerFactory.getLogger(ResourceKit.class);
    private final LocalizingTool localizingTool;
    private final RewritingTool rewritingTool;
    private final EvaluatingTool evaluatingTool;

    @Inject
    public ResourceKitImpl(LocalizingTool localizingTool,
                           RewritingTool rewritingTool,
                           EvaluatingTool evaluatingTool,
                           Polysupplier<ResourceOptionsPrototype> configs) {

        this.localizingTool = localizingTool;
        this.rewritingTool = rewritingTool;
        this.evaluatingTool = evaluatingTool;

        final LocalizationDigestImpl localizationDigest = new LocalizationDigestImpl(localizingTool);
        final RewritingDigestImpl rewritingDigest = new RewritingDigestImpl(rewritingTool);
        final PropertyDigestImpl propertyDigest = new PropertyDigestImpl(evaluatingTool);

        configs.forEach(c -> {
            c.addLocalizations(localizationDigest);
            c.addRewritings(rewritingDigest);
            c.addProperties(propertyDigest);
        }, null);

        if (log.isDebugEnabled()) {
            StringWriter writer = new StringWriter();
            propertyDigest.getEvaluatingTool().dumpProperties(writer);
            log.debug(writer.toString());
        }
    }

    @Override
    public Enumeration<URL> getURLs(String resourcePath) {
        try {
            Enumeration<URL> resources = getClassLoader().getResources(resourcePath);
            return resources;
        } catch (IOException e) {
            throw new ResourceException("Error reading resource URLs", e);
        }
    }

    @Override
    public InputStream getStream(String resourcePath) {
        InputStream in = getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new ResourceNotFoundException(resourcePath);
        }
        return in;
    }

    @Override
    public String localize(String resourcePath, L10NMode mode) {
        return localizingTool.localize(resourcePath, mode);
    }

    @Override
    public String localize(String resourcePath, L10NMode mode, ObjectiveQualifiers qualfiers) {
        return localizingTool.localize(resourcePath, mode, qualfiers);
    }

    @Override
    public final String rewrite(String resourcePath) {
        return rewritingTool.rewrite(resourcePath);
    }

    @Override
    public String evaluate(String resourcePath) {
        return evaluatingTool.evaluate(resourcePath);
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

}
