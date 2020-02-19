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
 * All possible qualifier names in standard order.
 * Order of qualifiers is important due localization process that defines look up order of  localization
 */
public final class QualifierStandard implements Iterable<String> {

    private final String[] names;

    public QualifierStandard(String[] names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    public int getSize() {
        return names.length;
    }

    public String getName(int i) {
        return names[i];
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(names).iterator();
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        for (String n : names) {
            action.accept(n);
        }
    }

    @Override
    public String toString() {
        return "CanonicalQualifiers{" +
                "names=" + Arrays.toString(names) +
                '}';
    }
}
