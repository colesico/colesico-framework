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

package colesico.framework.pebble;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.pebble.internal.FrameworkExtension;
import colesico.framework.pebble.internal.PebbleTemplateLoader;
import colesico.framework.weblet.HtmlRenderer;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.ViewResponse;
import colesico.framework.weblet.teleapi.WebletTWContext;
import colesico.framework.weblet.teleapi.writer.ViewWriter;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class PebbleTemplater extends ViewWriter implements HtmlRenderer {

    public static final String MODEL_VAR = "vm";

    private final PebbleEngine pebbleEngine;

    public PebbleTemplater(Provider<HttpContext> httpContextProv,
                           PebbleTemplateLoader tmplLoader,
                           FrameworkExtension frameworkExtension,
                           Polysupplier<PebbleOptionsPrototype> optionsSup) {
        super(httpContextProv);

        PebbleEngine.Builder builder = new PebbleEngine.Builder()
                .loader(tmplLoader)
                .extension(frameworkExtension)
                .autoEscaping(true)
                .defaultEscapingStrategy("html")
                .cacheActive(true);

        optionsSup.forEach(options -> options.applyOptions(builder));

        pebbleEngine = builder.build();
    }

    public <M> Writer evaluate(String templatePath, M viewModel) {
        Writer writer = new StringWriter();
        Map<String, Object> context = new HashMap<>();
        context.put(MODEL_VAR, viewModel);
        try {
            PebbleTemplate compiledTemplate = pebbleEngine.getTemplate(templatePath);
            compiledTemplate.evaluate(writer, context);
            return writer;
        } catch (PebbleException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(ViewResponse viewResponse, WebletTWContext context) {
        Writer writer = evaluate(viewResponse.getViewName(), viewResponse.getModel());

        HttpResponse httpResponse = httpContextProv.get().getResponse();

        HttpUtils.setHeaders(httpResponse, viewResponse.getHeaders());
        HttpUtils.setCookies(httpResponse, viewResponse.getCookies());

        String contentType = viewResponse.getContentType();
        if (StringUtils.isEmpty(contentType)) {
            contentType = HtmlResponse.DEFAULT_CONTENT_TYPE;
        }

        httpResponse.sendText(writer.toString(), contentType, viewResponse.getStatusCode());
    }

    /**
     * Renderer
     */
    @Override
    public String render(String templateName, Object model) {
        Writer writer = evaluate(templateName, model);
        return writer.toString();
    }
}
