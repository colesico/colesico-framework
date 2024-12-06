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

package colesico.framework.taskhub.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.taskhub.EventDispatcher;
import colesico.framework.taskhub.EventListener;
import colesico.framework.taskhub.binding.ListenerBinding;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class EventDispatcherImpl implements EventDispatcher {

    protected final Map<Class<?>, EventListeners<?>> eventBindings = new HashMap<>();

    @Inject
    public EventDispatcherImpl(Polysupplier<ListenerBinding<?>> listenersSupp) {
        initBindings(listenersSupp);
    }

    @SuppressWarnings("unchecked")
    protected final void initBindings(Polysupplier<ListenerBinding<?>> listenersSupp) {
        listenersSupp.forEach(
                listener -> {
                    var bindings = listener.getEventBindings();
                    for (var binding : bindings) {
                        var listeners = eventBindings.computeIfAbsent(binding.getEventClass(), c -> new EventListeners<>());
                        listeners.add(binding.getHandler());
                    }
                }, null
        );
    }


    @Override
    @SuppressWarnings("unchecked")
    public <E> void dispatch(E event) {
        var listeners = (EventListeners<E>) eventBindings.get(event.getClass());
        if (listeners != null) {
            for (var listener : listeners) {
                listener.onTask(event);
            }
        }
    }

    protected static class EventListeners<E> extends ArrayList<EventListener<E>> {

    }
}
