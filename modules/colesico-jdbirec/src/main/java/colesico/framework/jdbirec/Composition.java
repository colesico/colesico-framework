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

package colesico.framework.jdbirec;

import java.lang.annotation.*;


/**
 * Fields composition marker.  (analogue of JPA @Embedded)
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Compositions.class)
@Inherited
public @interface Composition {

    /**
     * Composition columns name prefix
     */
    String columnsPrefix() default "";

    /**
     * Columns to be imported from composition class  and subcomposition classes.
     * Imports only listed columns.
     * If not specified imports all not optional columns.
     * Column names are relative to the current composition chain.
     * It is possible to specify non direct columns i.e.: subCompField.column_1
     * Also possible to rename target column with = operator:  column_1=column_2
     *
     * @return
     */
    String[] columns() default {};

    /**
     * If specified creates composition object only if key column value is not null
     *
     * @return
     */
    String keyColumn() default "";

    /**
     * @return
     * @see Record#views()
     */
    String[] views() default {RecordView.ALL_VIEWS};

}
