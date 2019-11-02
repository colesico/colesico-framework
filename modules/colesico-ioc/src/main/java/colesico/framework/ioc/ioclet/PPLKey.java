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

package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.Key;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Post produce listener key.
 * Is used for obtaining the post produce listener from the IOC container to perform post produce handling.
 *
 * @author Vladlen Larionov
 * @see Key
 * @see colesico.framework.ioc.PostProduce
 */
public final class PPLKey<T> implements Key<T> {

    /**
     * Instance class name
     */
    private final String className;
    private final String withNamed;
    private final String withClassed;

    public PPLKey(Class<?> clazz, String withNamed, Class<?> withClassed) {
        this.className = clazz.getCanonicalName();
        this.withNamed = withNamed;
        if (withClassed != null) {
            this.withClassed = withClassed.getCanonicalName();
        } else {
            this.withClassed = null;
        }
    }

    public PPLKey(String className, String withNamed, String withClassed) {
        this.className = className;
        this.withNamed = withNamed;
        this.withClassed = withClassed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PPLKey<?> pplKey = (PPLKey<?>) o;

        return new EqualsBuilder()
            .append(className, pplKey.className)
            .append(withNamed, pplKey.withNamed)
            .append(withClassed, pplKey.withClassed)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(className)
            .append(withNamed)
            .append(withClassed)
            .toHashCode();
    }


    @Override
    public String toString() {
        return "PPLKey{" +
            "className='" + className + '\'' +
            ", withNamed='" + withNamed + '\'' +
            ", withClassed='" + withClassed + '\'' +
            '}';
    }
}