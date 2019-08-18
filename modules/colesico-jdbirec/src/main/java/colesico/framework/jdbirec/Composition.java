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

package colesico.framework.jdbirec;

import java.lang.annotation.*;


/**
 * Fields composition marker.  (analogue of JPA @Embedded)
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Composition {

    /**
     * Composition columns name prefix
     */
    String columnsPrefix() default "";

    /**
     * Recursive filter columns contained in the current composition and subcompositions.
     * Accept only listed columns.
     * If not specified accept all not optional columns.
     * Column names are relative to the current composition chain.
     * It is possible to specify non direct columns i.e.: subCompField.column_1
     * Also possible to rename target column with = operator:  column_1=column_2
     *
     * @return
     */
    String[] columns() default {};

    /**
     * @return
     * @see Record#profiles()
     */
    String[] profiles() default {};
}
