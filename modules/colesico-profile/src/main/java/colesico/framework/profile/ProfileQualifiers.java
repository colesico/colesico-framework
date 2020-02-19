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

package colesico.framework.profile;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Localization qualifier values from profile is used to select appropriate localized resource, etc.
 */
public final class ProfileQualifiers implements Iterable<String> {

    /**
     * Qualifiers
     * Number of qualifiers and its order must be in accordance with the specified in the profile configuration {@link ProfileConfigPrototype}.
     * Undetermined qualifier values must be null.
     */
    private final String[] values;

    public ProfileQualifiers(String[] values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(values).iterator();
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        for (String q : values) {
            action.accept(q);
        }
    }

    @Override
    public String toString() {
        return "ProfileQualifiers{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
