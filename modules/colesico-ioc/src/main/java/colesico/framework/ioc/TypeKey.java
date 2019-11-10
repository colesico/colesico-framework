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

package colesico.framework.ioc;

/**
 * The key for obtaining an instance from the IOC container by instance class.
 *
 * @see Key
 *
 * @author Vladlen Larionov
 */
public final class TypeKey<T> implements Key<T> {

    private final String className;

    public TypeKey(String className) {
        this.className = className;
    }

    public TypeKey(Class<T> clazz) {
        this.className = clazz.getCanonicalName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeKey typeKey = (TypeKey) o;

        return className.equals(typeKey.className);

    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        return "TypeKey{" +
                "className='" + className + '\'' +
                '}';
    }
}
