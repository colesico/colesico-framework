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

package colesico.framework.ioc;

/**
 * Used to store non IoC objects in a scope
 * @author Vladlen Larionov
 */
public final class StringKey<T> implements Key<T> {

    private final String str;

    public StringKey(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "StringKey{" +
                "str='" + str + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringKey<?> stringKey = (StringKey<?>) o;

        return str.equals(stringKey.str);

    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}
