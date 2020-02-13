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

package colesico.framework.ioc.key;

/**
 * The key by which the IoC container will find the factory to instantiate the class T annotated with @Classed
 * @param <T> instance type
 * @see Key
 */
public final class ClassedKey<T> implements Key<T> {
    private final String typeName;
    private final String classifier;

    public ClassedKey(String typeName, String classifier) {
        this.typeName = typeName;
        this.classifier = classifier;
    }

    public ClassedKey(Class<T> clazz, Class<?> classifier) {
        this.typeName = clazz.getCanonicalName();
        this.classifier = classifier.getCanonicalName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassedKey<?> that = (ClassedKey<?>) o;

        if (!typeName.equals(that.typeName)) return false;
        return classifier.equals(that.classifier);
    }

    @Override
    public int hashCode() {
        int result = typeName.hashCode();
        result = 31 * result + classifier.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ClassedKey{" +
                "className='" + typeName + '\'' +
                ", classifier='" + classifier + '\'' +
                '}';
    }
}
