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

package colesico.framework.eventbus;

public final class EventBinding<E> {

    private final Class<E> eventClass;
    private final EventHandler<E> handler;

    public EventBinding(Class<E> eventClass, EventHandler<E> handler) {
        this.eventClass = eventClass;
        this.handler = handler;
    }

    public Class<E> getEventClass() {
        return eventClass;
    }

    public EventHandler<E> getHandler() {
        return handler;
    }
}
