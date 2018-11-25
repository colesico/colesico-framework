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

package colesico.framework.assist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Elements collection
 * @author Vladlen Larionov
 */
public final class Elements<E> implements Iterable<E> {

    private final Collection<E> elements = new ArrayList<>();

    public Elements() {
    }

    public final void add(E element) {
        elements.add(element);
    }

    public final void addAll(Collection<E> elements) {
        this.elements.addAll(elements);
    }

    public E find(Function<E, Boolean> predicate) {
        for (E e : elements) {
            if (predicate.apply(e)) {
                return e;
            }
        }
        return null;
    }

    public Collection<E> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int size(){
        return elements.size();
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }
}
