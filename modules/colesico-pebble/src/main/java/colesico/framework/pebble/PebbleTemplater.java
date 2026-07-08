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

package colesico.framework.pebble;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpResponse;
import colesico.framework.http.assist.HttpUtils;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.pebble.internal.FrameworkExtension;
import colesico.framework.pebble.internal.PebbleTemplateLoader;
import colesico.framework.telehttp.ContentType;
import colesico.framework.weblet.response.ViewResponse;
import colesico.framework.weblet.WebletWriteOptions;
import colesico.framework.weblet.writer.ViewWriter;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class PebbleTemplater extends ViewWriter {

    public static final String MODEL_VAR = "vm";

    private final PebbleEngine pebbleEngine;

    public PebbleTemplater(Provider<HttpResponse> httpResponse,
                           PebbleTemplateLoader tmplLoader,
                           FrameworkExtension frameworkExtension,
                           Polysupplier<PebbleOptionsPrototype> optionsSup) {
        super(httpResponse);

        PebbleEngine.Builder builder = new PebbleEngine.Builder()
                .loader(tmplLoader)
                .extension(frameworkExtension)
                .autoEscaping(true)
                .defaultEscapingStrategy("html")
                .cacheActive(true);

        optionsSup.forEach(options -> options.applyOptions(builder));

        pebbleEngine = builder.build();
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.TEXT_HTML;
    }

    @Override
    protected void write(OutputStream outputStream, ViewResponse response, WebletWriteOptions options) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put(MODEL_VAR, response.model());
        Charset charset = contentType(response,options).charset();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {

            PebbleTemplate compiledTemplate = pebbleEngine.getTemplate(response.viewName());

            compiledTemplate.evaluate(writer, context);

            // 4. Flush to ensure all data is written to the underlying stream
            writer.flush();
        } catch (PebbleException | IOException e) {
            throw new RuntimeException(e);
        }
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
