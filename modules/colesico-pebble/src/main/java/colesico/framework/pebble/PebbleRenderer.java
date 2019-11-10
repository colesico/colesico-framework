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

package colesico.framework.pebble;

import colesico.framework.htmlrenderer.HtmlRenderer;
import colesico.framework.ioc.Polysupplier;
import colesico.framework.pebble.internal.FrameworkExtension;
import colesico.framework.pebble.internal.PebbleTemplateLoader;
import colesico.framework.weblet.HtmlResponse;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.inject.Inject;
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
public class PebbleRenderer implements HtmlRenderer<String> {

    public static final String MODEL_VAR = "vm";

    private final PebbleEngine pebbleEngine;

    @Inject
    public PebbleRenderer(PebbleTemplateLoader tmplLoader,
                          FrameworkExtension frameworkExtension,
                          Polysupplier<PebbleOptionsPrototype> optionsSup) {

        PebbleEngine.Builder builder = new PebbleEngine.Builder()
                .loader(tmplLoader)
                .extension(frameworkExtension)
                .autoEscaping(true)
                .defaultEscapingStrategy("html")
                .cacheActive(true);

        optionsSup.forEach(options -> options.applyOptions(builder), null);

        pebbleEngine = builder.build();
    }

    @Override
    public final <M> HtmlResponse render(String templatePath, M model) {
        Writer writer = evaluate(templatePath, model);
        return HtmlResponse.of(writer.toString());
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

}
