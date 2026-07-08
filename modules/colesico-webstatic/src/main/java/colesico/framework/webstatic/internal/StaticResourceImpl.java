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
package colesico.framework.webstatic.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceLocalizer;
import colesico.framework.resource.ResourceNotFoundException;
import colesico.framework.webstatic.MimeAssist;
import colesico.framework.webstatic.StaticResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Provider;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Vladlen Larionov
 */

public class StaticResourceImpl implements StaticResource {
    protected static final int SEND_BUFFER_SIZE = 8192;

    protected final Logger log = LoggerFactory.getLogger(StaticResource.class);

    protected final Provider<HttpContext> httpContextProv;
    protected final ResourceLocalizer resourceLocalizer;

    protected final String resourcesRoot;

    public StaticResourceImpl(Provider<HttpContext> httpContextProv, ResourceLocalizer resourceLocalizer, String resourcesRoot) {
        this.httpContextProv = httpContextProv;
        this.resourceLocalizer = resourceLocalizer;
        this.resourcesRoot = resourceLocalizer.localize(resourcesRoot);
    }

    @Override
    public void send(String resourceUri, boolean rewrite) {

        HttpContext httpContext = httpContextProv.get();

        String resourcePath = resourcesRoot + '/' + resourceUri;

        if (rewrite) {
            resourcePath = resourceLocalizer.localize(resourcePath);
        }

        httpContext.response().setContentType(MimeAssist.mimeType(resourcePath));

        try (InputStream is = resourceLocalizer.resourceStream(resourcePath);
             OutputStream os = httpContext.response().outputStream()) {
            byte[] buf = new byte[SEND_BUFFER_SIZE];
            int c;
            while ((c = is.read(buf, 0, buf.length)) > 0) {
                os.write(buf, 0, c);
                os.flush();
            }
        } catch (ResourceNotFoundException rnfe) {
            log.warn("Static resource not found: " + resourcePath);
        } catch (Exception e) {
            throw new ResourceException("Read resource '" + resourceUri + "->" + resourcePath + "' error", e);
        }
    }


}
