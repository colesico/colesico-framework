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

package colesico.framework.webstatic.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.ioc.message.InjectionPoint;
import colesico.framework.ioc.message.Message;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.resource.ResourceKit;
import colesico.framework.webstatic.StaticContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;

@Unscoped
public class StaticContentBuilderImpl implements StaticContent.Builder {

    protected Logger log = LoggerFactory.getLogger(StaticContent.Builder.class.getCanonicalName());
    protected final Provider<HttpContext> httpContextProv;
    protected final ResourceKit resourceKit;
    protected String resourcesRoot;

    public StaticContentBuilderImpl(
            @Message InjectionPoint injectionPoint,
            Provider<HttpContext> httpContextProv,
            ResourceKit resourceKit) {

        this.httpContextProv = httpContextProv;
        this.resourceKit = resourceKit;

        if (injectionPoint != null) {
            String moduleName = injectionPoint.getTargetClass().getModule().getName();
            if (moduleName != null) {
                String contentRootPkg = moduleName.replace('.', '/') + "/" + DEFAULT_RESOURCES_DIR;
                this.resourcesRoot = contentRootPkg;
            } else {
                throw new RuntimeException("Unnamed module for injection point: " + injectionPoint);
            }
        }
        log.debug("Initial content root: " + resourcesRoot);
    }

    @Override
    public StaticContentBuilderImpl resourcesRoot(String path) {
        this.resourcesRoot = path;
        return this;
    }

    @Override
    public StaticContent build() {
        if (StringUtils.isBlank(resourcesRoot)) {
            throw new RuntimeException("Undefined resources root");
        }
        return new StaticContentImpl(httpContextProv, resourceKit, resourcesRoot);
    }
}
