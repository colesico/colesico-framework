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

import colesico.framework.ioc.listener.PostProduce;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * Post produce listener key.
 * Is used for obtaining the post produce listener from the IOC container to perform post produce handling.
 *
 * @author Vladlen Larionov
 * @see Key
 * @see PostProduce
 */
public final class PPLKey<T> implements Key<T> {

    /**
     * Instance class name
     */
    private final String typeName;
    private final String withNamed;
    private final String withClassed;

    public PPLKey(Class<?> clazz, String withNamed, Class<?> withClassed) {
        this.typeName = clazz.getCanonicalName();
        this.withNamed = withNamed;
        if (withClassed != null) {
            this.withClassed = withClassed.getCanonicalName();
        } else {
            this.withClassed = null;
        }
    }

    public PPLKey(String typeName, String withNamed, String withClassed) {
        this.typeName = typeName;
        this.withNamed = withNamed;
        this.withClassed = withClassed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPLKey<?> pplKey = (PPLKey<?>) o;
        return typeName.equals(pplKey.typeName) &&
                Objects.equals(withNamed, pplKey.withNamed) &&
                Objects.equals(withClassed, pplKey.withClassed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, withNamed, withClassed);
    }

    @Override
    public String toString() {
        return "PPLKey{" +
            "className='" + typeName + '\'' +
            ", withNamed='" + withNamed + '\'' +
            ", withClassed='" + withClassed + '\'' +
            '}';
    }
}
