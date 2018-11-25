/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */
package colesico.framework.resource.internal;

import colesico.framework.ioc.Polysupplier;
import colesico.framework.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
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
    private final EvaluationTool evaluationTool;

    @Inject
    public ResourceKitImpl(LocalizingTool localizingTool,
                           RewritingTool rewritingTool,
                           EvaluationTool evaluationTool,
                           Polysupplier<ResourceConfig> configs) {

        this.localizingTool = localizingTool;
        this.rewritingTool = rewritingTool;
        this.evaluationTool = evaluationTool;

        final QualifiersBinderImpl qualifiersBinder = new QualifiersBinderImpl(localizingTool);
        final RewritingsBinderImpl rewritingsBinder = new RewritingsBinderImpl(rewritingTool);
        final PropertiesBinderImpl propertiesBinder = new PropertiesBinderImpl(evaluationTool);

        configs.forEach(c -> {
            c.bindQualifiers(qualifiersBinder);
            c.bindRewritings(rewritingsBinder);
            c.bindProperties(propertiesBinder);
        }, null);

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
    public String localize(String resourcePath, L10NMode mode, String[] qualfiers) {
        return localizingTool.localize(resourcePath, mode, qualfiers);
    }

    @Override
    public final String rewrite(String resourcePath) {
        return rewritingTool.rewrite(resourcePath);
    }

    @Override
    public String evaluate(String resourcePath) {
        return evaluationTool.evaluate(resourcePath);
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

}
