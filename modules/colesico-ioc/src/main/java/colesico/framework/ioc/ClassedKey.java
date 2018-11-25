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
 * The key by which the IoC container will find the factory to instantiate the class T annotated with @Classed
 * @param <T>
 * @see Key
 */
public final class ClassedKey<T> implements Key<T> {
    private final String className;
    private final String classifier;

    public ClassedKey(String className, String classifier) {
        this.className = className;
        this.classifier = classifier;
    }

    public ClassedKey(Class<T> clazz, Class<?> classifier) {
        this.className = clazz.getCanonicalName();
        this.classifier = classifier.getCanonicalName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassedKey<?> that = (ClassedKey<?>) o;

        if (!className.equals(that.className)) return false;
        return classifier.equals(that.classifier);
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + classifier.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ClassedKey{" +
                "className='" + className + '\'' +
                ", classifier='" + classifier + '\'' +
                '}';
    }
}
