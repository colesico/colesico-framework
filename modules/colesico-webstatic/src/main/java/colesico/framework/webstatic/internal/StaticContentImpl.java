/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.webstatic.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceKit;
import colesico.framework.webstatic.MimeAssist;
import colesico.framework.webstatic.MimeType;
import colesico.framework.webstatic.StaticContent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Vladlen Larionov
 */

public class StaticContentImpl implements StaticContent {
    protected static final int SEND_BUFFER_SIZE = 8192;

    protected final Logger log = LoggerFactory.getLogger(StaticContent.class);

    protected final Provider<HttpContext> httpContextProv;
    protected final ResourceKit resourceKit;

    protected final String resourcesRoot;

    public StaticContentImpl(Provider<HttpContext> httpContextProv, ResourceKit resourceKit, String resourcesRoot) {
        this.httpContextProv = httpContextProv;
        this.resourceKit = resourceKit;
        this.resourcesRoot = resourceKit.evaluate(resourcesRoot);
    }

    @Override
    public void send(String resourceUri, ResourceKit.L10NMode mode) {

        HttpContext httpContext = httpContextProv.get();

        String resourcePath = resourcesRoot + '/' + resourceUri;
        resourcePath = resourceKit.localize(resourcePath, mode);
        resourcePath = resourceKit.rewrite(resourcePath);

        httpContext.getResponse().setContenType(MimeAssist.getContentType(resourcePath));

        try (InputStream is = resourceKit.getStream(resourcePath);
             OutputStream os = httpContext.getResponse().getOutputStream()) {
            byte[] buf = new byte[SEND_BUFFER_SIZE];
            int c = 0;
            while ((c = is.read(buf, 0, buf.length)) > 0) {
                os.write(buf, 0, c);
                os.flush();
            }
        } catch (Exception ex) {
            throw new ResourceException("Read resource '" + resourceUri + "->" + resourcePath + "' error", ex);
        }
    }



}
