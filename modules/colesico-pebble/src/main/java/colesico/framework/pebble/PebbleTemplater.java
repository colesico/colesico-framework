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

import colesico.framework.http.HttpResponse;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

        optionsSup.forEach(options -> options.configure(builder));

        pebbleEngine = builder.build();
    }

    @Override
    protected ContentType defaultContentType() {
        return ContentType.TEXT_HTML;
    }

    @Override
    protected void write(OutputStream outputStream, ViewResponse response, WebletWriteOptions options) throws IOException {
        Map<String, Object> context;
        if (response.model() instanceof Map m) {
            context = m;
        } else {
            context = new HashMap<>();
            context.put(MODEL_VAR, response.model());
        }
        Charset charset = contentType(response, options).charset().orElse(StandardCharsets.UTF_8);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            PebbleTemplate compiledTemplate = pebbleEngine.getTemplate(response.view());
            compiledTemplate.evaluate(writer, context);
            writer.flush();
        } catch (PebbleException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String render(String templateName, Object model) {
        Map<String, Object> context = new HashMap<>();
        context.put(MODEL_VAR, model);
        PebbleTemplate compiledTemplate = pebbleEngine.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        try {
            compiledTemplate.evaluate(writer, context);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
