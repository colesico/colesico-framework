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

package colesico.framework.pebble.internal;

import colesico.framework.resource.ResourceKit;
import io.pebbletemplates.pebble.error.LoaderException;
import io.pebbletemplates.pebble.loader.Loader;
import io.pebbletemplates.pebble.utils.PathUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class PebbleTemplateLoader implements Loader<String> {

    protected final ResourceKit resourceKit;
    protected final Logger logger = LoggerFactory.getLogger(PebbleTemplateLoader.class);

    //private String prefix;
    private String suffix = ".html";
    private String charset = "UTF-8";

    @Inject
    public PebbleTemplateLoader(ResourceKit resourceKit) {
        this.resourceKit = resourceKit;
    }

    @Override
    public Reader getReader(String templateName) throws LoaderException {
        String resourceName = getResourceName(templateName);
        Reader reader = null;
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                if (reader != null) {
                    logger.warn("Duplicate template for " + resourceName + " ignored: " + resourceUrl);
                } else {
                    InputStreamReader isr = new InputStreamReader(resourceUrl.openStream(), charset);
                    reader = new BufferedReader(isr);
                    logger.debug("Template " + resourceName + " located: " + resourceUrl);
                }
            }
            if (reader == null) {
                throw new RuntimeException("Template not found: " + resourceName);
            }
        } catch (Exception e) {
            throw new LoaderException(e, "Error loading template: " + templateName);
        }


        return reader;
    }

    protected String getResourceName(String templateName) {
        String resourceName = resourceKit.localize(templateName);

        if (!StringUtils.endsWith(resourceName, suffix)) {
            resourceName = resourceName + suffix;
        }

        return resourceName;
    }


    @Override
    public void setCharset(String s) {
        charset = s;
    }

    @Override
    public void setPrefix(String s) {
        //prefix = s;
    }

    @Override
    public void setSuffix(String s) {
        suffix = s;
    }

    @Override
    public String resolveRelativePath(String relativePath, String anchorPath) {
        return PathUtils.resolveRelativePath(relativePath, anchorPath, '/');
    }

    @Override
    public String createCacheKey(String templateName) {
        return templateName;
    }

    @Override
    public boolean resourceExists(String templateName) {
        String resourcePath = getResourceName(templateName);
        Enumeration<URL> resources = resourceKit.getResourceURLs(resourcePath);
        return resources.hasMoreElements();
    }
}
