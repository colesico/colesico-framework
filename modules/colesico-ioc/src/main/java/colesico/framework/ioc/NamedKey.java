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
 * The key for obtaining an instance from the IoC container by instance class and name.
 *
 * @see javax.inject.Named
 * @author Vladlen Larionov
 */
public final class NamedKey<T> implements Key<T> {

    private final String typeName;
    private final String name;

    public NamedKey(String typeName, String name) {
        this.typeName = typeName;
        this.name = name;
    }

    public NamedKey(Class<T> clazz, String name) {
        this.typeName = clazz.getCanonicalName();
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedKey namedKey = (NamedKey) o;

        if (!typeName.equals(namedKey.typeName)) return false;
        return name.equals(namedKey.name);
    }

    @Override
    public int hashCode() {
        int result = typeName.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NamedKey{" +
                "className='" + typeName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
