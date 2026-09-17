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

import colesico.framework.assist.StringUtils;
import colesico.framework.translation.TranslationBundle;
import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Override production assets url (js, css files) to dev server
 */
public final class DevUrlFilter implements Filter {

    public static final String FILTER_NAME = "devUrl";

    /**
     * UI development url env variable name
     */
    public static String UI_DEV_URL_ENV = "UI_DEV_URL";

    /**
     * UI development url  system property name
     */
    public static String UI_DEV_URL_ARG = "ui.dev.url";

    public DevUrlFilter() {
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate pebbleTemplate, EvaluationContext evaluationContext, int lineNumber) throws PebbleException {

        if (!(input instanceof String)) {
            throw new PebbleException(null,
                    FILTER_NAME + " filter can be applied only to  string value. Current value=" + input,
                    lineNumber, pebbleTemplate.getName());
        }
        String prodUrl = (String) input;

        var devUrl = (String) args.get(String.valueOf(0));

        var devServer = System.getenv(UI_DEV_URL_ENV);
        if (StringUtils.isBlank(devServer)) {
            devServer = System.getProperty(UI_DEV_URL_ARG);
        }

        if (StringUtils.isBlank(devServer)) {
            return prodUrl;
        } else {
            return devServer + devUrl;
        }
    }
}
