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
     *
     * @param objs
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object... objs) {
        List<T> result = new ArrayList<>();
        for (Object o : objs) {
            if (o.getClass().isArray()) {
                Object[] oa = (Object[]) o;
                for (Object i:oa){
                    result.add((T) i);
                }
            } else {
                result.add((T) o);
            }
        }
        return result;
    }

    /**
     * Builds the array with arguments as its elements
     *
     * @param objs
     * @return
     */
    public static Object[] toArray(Object... objs) {
        List result = toList(objs);
        return result.toArray();
    }

    /**
     * Builds set of annotation from arguments
     *
     * @param ann
     * @return
     */
    public static Set<Class<? extends Annotation>> annotationClassSet(Class<? extends Annotation>... ann) {
        Set<Class<? extends Annotation>> result = new HashSet<>();
        result.addAll(Arrays.asList(ann));
        return result;
    }
}
