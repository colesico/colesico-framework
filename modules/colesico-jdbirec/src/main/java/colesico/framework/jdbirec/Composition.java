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
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Compositions.class)
@Inherited
public @interface Composition {

    /**
     * Composition name.
     * Alias for composition field name.
     * If not specified, will take the value of the composition field name.
     * This name is useful for composition columns renaming.
     */
    String name() default "";

    /**
     * Tag filter for nested columns and compositions
     *
     * @see TagFilter()
     * @see Column#tags()
     */
    TagFilter tagFilter() default @TagFilter;


    // Substitution to composition name + '_' + column name
    String RN_PREFIX = "@prefix";

    // Substitution to origin column name
    String RN_COLUMN_NAME = "@column";

    // Substitution to column composition name
    String RN_COMPOSITION_NAME = "@composition";

    /**
     * Composition columns renaming strategy.
     * <p>
     * If not specified, the column names remain unchanged.
     * Examples:
     * foo_@column_bar ->  foo_{@link Column#name()}_bar
     * foo_@field_bar -> foo_[composition filed name]_bar
     * </p>
     */
    String renaming() default "";

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
     * @see TagFilter
     */
    String[] tags() default {};
}
