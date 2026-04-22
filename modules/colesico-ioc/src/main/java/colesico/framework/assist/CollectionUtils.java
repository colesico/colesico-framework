/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.assist;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Helper class designed to work with collections
 *
 * @author Vladlen Larionov
 */
public class CollectionUtils {

    /**
     * Builds the list with arguments as its elements
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object... objs) {
        List<T> result = new ArrayList<>();
        for (Object o : objs) {
            if (o instanceof Object[] oa) {
                result.addAll((List<T>) Arrays.asList(oa));
            } else {
                result.add((T) o);
            }
        }
        return result;
    }
    /**
     * Builds the array with arguments as its elements
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(T... objs) {
        List<T> result = toList(objs);
        return (T[]) result.toArray();
    }

}
