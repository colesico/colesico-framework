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

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.ResourceNotFoundException;
import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.RewritingPhase;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class ResourceKitImpl implements ResourceKit {

    protected final Logger log = LoggerFactory.getLogger(ResourceKit.class);

    protected List<PathRewriter> rewriters = new ArrayList<>();

    @Inject
    public ResourceKitImpl(Polysupplier<PathRewriter> rewritersSupp) {

        final PhaseMapper phaseMapper = new PhaseMapper();
        rewritersSupp.forEach(phaseMapper::add);

        for (RewritingPhase ph : RewritingPhase.values()) {
            List<PathRewriter> phaseRewList = phaseMapper.getRewriters(ph);
            if (phaseRewList == null) {
                continue;
            }
            rewriters.addAll(phaseRewList);
        }

    }

    @Override
    public String rewrite(String path) {
        for (PathRewriter rewriter : rewriters) {
            path = rewriter.rewrite(path);
        }
        return path;
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
