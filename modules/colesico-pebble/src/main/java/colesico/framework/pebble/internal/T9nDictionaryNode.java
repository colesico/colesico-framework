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
import colesico.framework.translation.TranslationKit;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Vladlen Larionov
 */
public final class T9nDictionaryNode extends AbstractRenderableNode {

    private final TranslationKit translationKit;
    private final String dictName;
    private final String basePath;

    public T9nDictionaryNode(int lineNumber, TranslationKit translationKit, String dictName, String basePath) {
        super(lineNumber);
        this.translationKit = translationKit;
        this.dictName = dictName;
        this.basePath = basePath;
    }

    @Override
    public void render(PebbleTemplateImpl pebbleTemplate, Writer writer, EvaluationContextImpl context) throws IOException {
        TranslationBundle dictionary = translationKit.getBundle(basePath);

        // local variable
        context.getScopeChain().put(dictName, dictionary);

        // global variable
        //GlobalContext globalContext = (GlobalContext) context.getVariable("_context");
        //globalContext.put(dictName, dictionary);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
