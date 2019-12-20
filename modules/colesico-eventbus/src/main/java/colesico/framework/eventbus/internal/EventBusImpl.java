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

import colesico.framework.eventbus.EventBinding;
import colesico.framework.eventbus.EventBus;
import colesico.framework.eventbus.EventHandler;
import colesico.framework.eventbus.EventsListener;
import colesico.framework.ioc.Polysupplier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EventBusImpl implements EventBus {

    protected final Map<Class<?>, List<EventHandler<?>>> eventsBindings = new HashMap();

    @Inject
    public EventBusImpl(Polysupplier<EventsListener> listenersSupp) {
        initBindings(listenersSupp);
    }

    protected final void initBindings(Polysupplier<EventsListener> listenersSupp) {
        listenersSupp.forEach(
                listener -> {
                    EventBinding[] bindings = listener.getBindings();
                    for (EventBinding binding : bindings) {
                        List<EventHandler<?>> handlers = eventsBindings.computeIfAbsent(binding.getEventClass(), c -> new ArrayList<>());
                        handlers.add(binding.getHandler());
                    }
                }, null
        );
    }

    @Override
    public <E> void send(E event) {
        List<EventHandler<?>> handlers = eventsBindings.get(event.getClass());
        if (handlers != null) {
            for (EventHandler handler : handlers) {
                handler.onEvent(event);
            }
        }
    }
}
