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

package colesico.framework.taskhub.binding;

import javax.inject.Provider;

abstract public class ListenerBinding<S> {

    public static final String GET_BINDINGS_METHOD = "getEventBindings";
    public static final String TARGET_PROV_FIELD = "targetProv";

    protected final Provider<S> targetProv;

    public ListenerBinding(Provider<S> targetProv) {
        this.targetProv = targetProv;
    }

    abstract public EventBinding[] getEventBindings();
}