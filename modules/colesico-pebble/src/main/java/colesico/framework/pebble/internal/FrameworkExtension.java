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

import colesico.framework.translation.TranslationKit;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import javax.inject.Singleton;
import java.util.*;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class FrameworkExtension extends AbstractExtension {

    private final TranslationKit t9n;

    public FrameworkExtension(TranslationKit t9n) {
        this.t9n = t9n;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<>();
        filters.put(T9nFilter.FILTER_NAME, new T9nFilter(t9n));
        return filters;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> tokenParsers = new ArrayList<>();
        tokenParsers.add(new T9nDictionaryParser(t9n));
        return tokenParsers;
    }
}
