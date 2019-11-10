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

package colesico.framework.http;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Vladlen Larionov
 */
public final class MultiValue<T> {
    private final Collection<T> values;

    public MultiValue(Collection<T> values) {
        this.values = values;
    }

    public T value() {
        Iterator<T> iterator = values.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public int size() {
        return values.size();
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Iterator<T> iterator() {
        return values.iterator();
    }

}
