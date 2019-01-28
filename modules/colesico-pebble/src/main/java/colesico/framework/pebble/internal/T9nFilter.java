/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.pebble.internal;

import colesico.framework.translation.Bundle;
import colesico.framework.translation.TranslationKit;
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

    protected final TranslationKit t9n;

    public T9nFilter(TranslationKit t9n) {
        this.t9n = t9n;
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

        Bundle dictionary = null;
        List<String> textParams = null;

        Object param0 = args.get(String.valueOf(0));
        Object param1 = args.get(String.valueOf(1));

        if (param0 instanceof Bundle) {
            dictionary = (Bundle) param0;
            textParams = (List<String>) param1;
        } else {
            dictionary = (Bundle) evaluationContext.getVariable(T9nDictionaryParser.DEFAULT_DICT_NAME);
            textParams = (List<String>) param0;
        }

        if (dictionary == null) {
            throw new PebbleException(null, "Translation dictionary not found", lineNumber, pebbleTemplate.getName());
        }

        if (textParams == null || textParams.isEmpty()) {
            return dictionary.get(strKey, strKey);
        }

        return dictionary.get(strKey, strKey, textParams.toArray());

    }
}