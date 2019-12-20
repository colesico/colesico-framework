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

package colesico.framework.ioc;

/**
 * The key for obtaining an instance from the IOC container by instance class.
 *
 * @see Key
 *
 * @author Vladlen Larionov
 */
public final class TypeKey<T> implements Key<T> {

    private final String typeName;

    public TypeKey(String typeName) {
        this.typeName = typeName;
    }

    public TypeKey(Class<T> clazz) {
        this.typeName = clazz.getCanonicalName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeKey typeKey = (TypeKey) o;

        return typeName.equals(typeKey.typeName);

    }

    @Override
    public int hashCode() {
        return typeName.hashCode();
    }

    @Override
    public String toString() {
        return "TypeKey{" +
                "className='" + typeName + '\'' +
                '}';
    }
}
