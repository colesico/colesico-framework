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
 * Fields composition marker.
 * Analogue of JPA @Embedded.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Compositions.class)
@Inherited
public @interface Composition {

    // Substitution to composition field name + column name
    String RN_AUTO = "@auto";

    // Substitution to origin column name
    String RN_COLUMN = "@column";

    // Substitution to column field name
    String RN_COLUMN_FILED = "@column-field";

    // Substitution to column composition name
    String RN_COMPOSITION = "@composition";

    // Substitution to column composition field name
    String RN_COMPOSITION_FIELD = "@composition-field";

    /**
     * Composition name.
     * Default value is derived from composition field name
     */
    String name() default "";

    /**
     * Tag filter for nested columns and compositions
     *
     * @see RecordView#tagFilter()
     * @see Column#tags()
     */
    String tagFilter() default RecordView.ALL_TAGS_FILTER;

    /**
     * Composition columns renaming strategy.
     * <p>
     * If not specified, the column names remain unchanged.
     * Examples:
     * foo_@column_bar ->  foo_{@link Column#name()}_bar
     * foo_@field_bar -> foo_[composition filed name]_bar
     * </p>
     */
    String renaming() default RN_AUTO;

    /**
     * Composition columns overriding
     */
    ColumnOverriding[] columnOverriding() default {};

    /**
     * If FALSE, creates composition object only if any column value is not null,
     * otherwise the composition object will be created regardless of the column values,
     * i.e. even if all column values are null
     */
    boolean nullInstace() default true;

    /**
     * Composition tags
     *
     * @see RecordView#tagFilter()
     */
    String[] tags() default {};
}
