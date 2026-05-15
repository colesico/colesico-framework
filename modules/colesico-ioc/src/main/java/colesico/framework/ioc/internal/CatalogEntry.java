/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.ioc.internal;

import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.key.Key;

public final class CatalogEntry<T> {

    private final Key<T> key;
    private final Condition condition;
    private final Substitution substitution;
    private final Integer polyproduce;

    private EntryAction action;

    private Factory<T> factory;

    public CatalogEntry(Key<T> key, Condition condition, Substitution substitution, Integer polyproduce) {
        this.key = key;
        this.condition = condition;
        this.substitution = substitution;
        this.polyproduce = polyproduce;
        this.action = EntryAction.NONE;
    }

    public Key<T> key() {
        return key;
    }

    public Condition condition() {
        return condition;
    }

    public Substitution substitution() {
        return substitution;
    }

    public Integer getPolyproduce() {
        return polyproduce;
    }

    public Factory<T> factory() {
        return factory;
    }

    public void setFactory(Factory<T> factory) {
        this.factory = factory;
    }

    public EntryAction action() {
        return action;
    }

    public void setAction(EntryAction action) {
        this.action = action;
    }
}
