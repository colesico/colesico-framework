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

package colesico.framework.pebble.internal;

import colesico.framework.translation.TranslationKit;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import org.apache.commons.lang3.StringUtils;

/**
 * Translation dictionary loader.
 * <p>
 * Loads dictionary to template scoped variable.
 * Loaded dictionary my be used by t9n filter.
 *
 * Usage examples:
 * <p>
 * {% t9nDictionary "dictionary/base/path" %} - loads dictionary to 'messages' variable
 * {% t9nDictionary "dictionary/base/path" "myName" %} - loads dictionary to 'myName' variable
 *
 * @see T9nFilter
 * @author Vladlen Larionov
 */
public final class T9nDictionaryParser implements TokenParser {

    public static final String DEFAULT_DICT_NAME = "messages";

    private final TranslationKit translationKit;

    public T9nDictionaryParser(TranslationKit translationKit) {
        this.translationKit = translationKit;
    }

    @Override
    public String getTag() {
        return "t9nDictionary";
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // dictionary name
        String dictName;
        // dictionary base path
        String basePath;

        // skip the "t9nDictionary" token
        token = stream.next();


        // next token is String?
        if (token.test(Token.Type.STRING)) {
            basePath = token.getValue();
            if (StringUtils.isBlank(basePath)) {
                throw new ParserException(null, "Empty translation dictionary base path", lineNumber, parser.getStream().getFilename());
            }
        } else {
            throw new ParserException(null, "Translation dictionary base path expected", lineNumber, parser.getStream().getFilename());
        }

        token = stream.next();

        if (token.test(Token.Type.STRING)) {
            dictName = token.getValue();
            if (StringUtils.isBlank(dictName)) {
                dictName = DEFAULT_DICT_NAME;
            }
            stream.next();
        } else {
            dictName = DEFAULT_DICT_NAME;
        }

        // expect to see "%}"
        stream.expect(Token.Type.EXECUTE_END);
        return new T9nDictionaryNode(lineNumber, translationKit, dictName, basePath);
    }
}
