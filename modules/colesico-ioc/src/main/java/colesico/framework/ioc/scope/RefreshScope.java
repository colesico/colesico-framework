/*
 * Copyright © 2014-2023 Vladlen V. Larionov and others as noted.
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

package colesico.framework.ioc.scope;

import colesico.framework.ioc.key.Key;

/**
 * Refresh scope interface.
 * Suitable  for config hot reload beans, e.t.c.
 *
 * @author Vladlen Larionov
 */
public interface RefreshScope extends Scope {

    /**
     * Recreate existing bean instance referenced by key within the scope.
     *
     * @return bean new instance if previous one existed, null otherwise
     */
    <T, M> T refresh(Key<T> key, M message);

    /**
     * Alias for {@link #refresh(Key, Object)}
     */
    <T> T refresh(Class<T> type);

    /**
     * Recreate all beans within the scope
     */
    void refreshAll();
}
