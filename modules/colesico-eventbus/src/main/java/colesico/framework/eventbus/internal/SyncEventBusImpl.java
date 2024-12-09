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

package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.SyncEventBus;
import colesico.framework.eventbus.registry.EventRegistry;
import colesico.framework.eventbus.registry.ListenersGroup;

import javax.inject.Singleton;

@Singleton
public class SyncEventBusImpl implements SyncEventBus {

    private final EventRegistry registry;

    public SyncEventBusImpl(EventRegistry registry) {
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> void dispatch(final E event) {
        var listeners = (ListenersGroup<E>) registry.getEventListeners(event.getClass());
        if (listeners != null) {
            listeners.apply(listener -> listener.consume(event));
        }
    }


}
