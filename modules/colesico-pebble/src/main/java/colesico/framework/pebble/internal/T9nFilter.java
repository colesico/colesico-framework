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

package colesico.framework.pebble.internal;

import colesico.framework.translation.TranslationBundle;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * This filter perform string translation by previously loaded dictionary  (with t9nDictionary tag)
 * <p>
 * Filter usage example:
 * {{ "myStringKey" | t9n }} - for dictionary name 'messages'
 * {{ "myStringKey" | t9n(myName) }} - for dictionary name 'myName'
 * {{ "myStringKey" | t9n([param1, param2]) }} - for dictionary name 'messages'
 * {{ "myStringKey" | t9n(myName,[param1, param2]) }} - for dictionary name 'myName'
 *
 * @author Vladlen Larionov
 * @see T9nDictionaryParser
 */

public final class T9nFilter implements Filter {

    public static final String FILTER_NAME = "t9n";

    public T9nFilter() {
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate pebbleTemplate, EvaluationContext evaluationContext, int lineNumber) throws PebbleException {

        if (!(input instanceof String)) {
            throw new PebbleException(null,
                    FILTER_NAME + " filter can be applied only to string value. Current value=" + input,
                    lineNumber, pebbleTemplate.getName());
        }
        String strKey = (String) input;

        TranslationBundle dictionary;
        List<String> textParams;

        Object param0 = args.get(String.valueOf(0));
        Object param1 = args.get(String.valueOf(1));

        if (param0 instanceof TranslationBundle) {
            dictionary = (TranslationBundle) param0;
            textParams = (List<String>) param1;
        } else {
            dictionary = (TranslationBundle) evaluationContext.getVariable(T9nDictionaryParser.DEFAULT_DICT_NAME);
            textParams = (List<String>) param0;
        }

        if (dictionary == null) {
            StringBuilder sb = new StringBuilder("args: ");
            for (Map.Entry<String, Object> me : args.entrySet()) {
                sb.append(me.getKey()).append("=").append(me.getValue()).append("; ");
            }
            throw new PebbleException(null, "Translation dictionary not found. (" + sb.toString() + ")", lineNumber, pebbleTemplate.getName());
        }

        if (textParams == null || textParams.isEmpty()) {
            return dictionary.get(strKey, strKey);
        }

        return dictionary.get(strKey, strKey, textParams.toArray());

    }
}
