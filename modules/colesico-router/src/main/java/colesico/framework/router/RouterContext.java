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

package colesico.framework.router;

import colesico.framework.ioc.Key;
import colesico.framework.ioc.TypeKey;

import java.util.Collections;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public final class RouterContext {

    public static Key<RouterContext> SCOPE_KEY = new TypeKey<>(RouterContext.class);

    protected final String uri;
    protected final Map<String, String> parameters;

    public RouterContext(String uri, Map<String, String> paramsMap) {
        this.uri = uri;
        this.parameters = Collections.unmodifiableMap(paramsMap);
    }

    public String getUri() {
        return uri;
    }

    /**
     * Returns unmodifiable route deserialize model
     *
     * @return
     */
    public Map<String, String> getParameters() {
        return parameters;
    }
}
