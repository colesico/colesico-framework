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
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * This function perform string translation by previously loaded dictionary  (with t9nDictionary tag)
 * <p>
 * Filter usage example:
 * {{ t9n("strKey") }} - for dictionary name 'messages'
 * {{ t9n("strKey", myName) }} - for dictionary name 'myName'
 * {{ t9n("strKey", [param1, param2]) }} - for dictionary name 'messages'
 * {{ t9n("myStringKey" , myName,[param1, param2]) }} - for dictionary name 'myName'
 *
 * @author Vladlen Larionov
 * @see T9nDictionaryParser
 */
public class T9nFunction implements Function {
    public static final String FUNCTION_NAME = "t9n";

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate pebbleTemplate, EvaluationContext context, int lineNumber) {

        String strKey;
        TranslationBundle dictionary;
        List<String> textParams;

        Object param0 = args.get(String.valueOf(0));
        if (!(param0 instanceof String)) {
            throw new PebbleException(null, "Translation key is not a string:" + param0, lineNumber, pebbleTemplate.getName());
        }
        strKey = (String) param0;


        Object param1 = args.get(String.valueOf(1));
        Object param2 = args.get(String.valueOf(2));

        if (param1 instanceof TranslationBundle) {
            dictionary = (TranslationBundle) param1;
            textParams = (List<String>) param2;
        } else {
            dictionary = (TranslationBundle) context.getVariable(T9nDictionaryParser.DEFAULT_DICT_NAME);
            //if(dictionary ==null){
            //    GlobalContext globalContext = (GlobalContext) context.getVariable("_context");
            //    dictionary = (Bundle) globalContext.get(T9nDictionaryParser.DEFAULT_DICT_NAME);
            //}
            textParams = (List<String>) param1;
        }

        if (dictionary == null) {
            throw new PebbleException(null, "Translation dictionary not found", lineNumber, pebbleTemplate.getName());
        }

        if (textParams == null || textParams.isEmpty()) {
            return dictionary.get(strKey, strKey);
        }

        return dictionary.get(strKey, strKey, textParams.toArray());
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
