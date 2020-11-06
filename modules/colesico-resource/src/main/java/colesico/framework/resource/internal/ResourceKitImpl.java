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
import colesico.framework.resource.*;
import colesico.framework.resource.rewriters.PrefixRewriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class ResourceKitImpl implements ResourceKit {

    protected final Logger log = LoggerFactory.getLogger(ResourceKit.class);

    protected List<PathRewriter> rewriters = new ArrayList<>();

    @Inject
    public ResourceKitImpl(Polysupplier<ResourceOptionsPrototype> configs) {

        final Map<RewritingPhase, List<PathRewriter>> rewritersMap = new HashMap<>();
        configs.forEach(cfg -> {
            List<PathRewriter> cfgRewList = cfg.getRewriters();
            for (PathRewriter rew : cfgRewList) {
                List<PathRewriter> phaseRewList = rewritersMap.computeIfAbsent(rew.phase(), ph -> new ArrayList<>());
                phaseRewList.add(rew);
            }
        }, null);

        for (RewritingPhase ph : RewritingPhase.values()) {
            List<PathRewriter> phaseRewList = rewritersMap.get(ph);
            if (phaseRewList == null) {
                continue;
            }
            rewriters.addAll(phaseRewList);
        }

    }

    @Override
    public Enumeration<URL> getResourceURLs(String resourcePath) {
        try {
            Enumeration<URL> resources = getClassLoader().getResources(resourcePath);
            return resources;
        } catch (IOException e) {
            throw new ResourceException("Error reading resource URLs", e);
        }
    }

    @Override
    public InputStream getResourceStream(String resourcePath) {
        InputStream in = getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new ResourceNotFoundException(resourcePath);
        }
        return in;
    }

    @Override
    public final String rewrite(String path) {
        for (PathRewriter rew : rewriters) {
            path = rew.rewrite(path);
        }
        return path;
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

}
