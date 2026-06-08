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

import java.util.Objects;

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

    public Integer polyproduce() {
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CatalogEntry<?> that)) return false;
        return Objects.equals(key, that.key) && Objects.equals(condition, that.condition) && substitution == that.substitution && Objects.equals(polyproduce, that.polyproduce) && action == that.action && Objects.equals(factory, that.factory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, condition, substitution, polyproduce, action, factory);
    }

    @Override
    public String toString() {
        return "CatalogEntry{" +
                "key=" + key +
                ", condition=" + condition +
                ", substitution=" + substitution +
                ", polyproduce=" + polyproduce +
                ", action=" + action +
                ", factory=" + factory +
                '}';
    }
}
