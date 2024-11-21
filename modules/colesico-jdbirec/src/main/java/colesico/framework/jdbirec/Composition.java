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

    // Composition field name + column name naming
    String AUTO_REF ="@auto";


    // Column name reference
    String COLUMN_REF="@column";

    // Column field name reference
    String COLUMN_FILED_REF ="@column-field";

    // Column composition name reference
    String COMPOSITION_REF="@composition";

    // Column composition field name reference
    String COMPOSITION_FIELD_REF ="@composition-field";

    // Composition name. Default as composition field name
    String name() default "";

    /**
     * Composition field renaming strategy.
     * If not specified, the field names remain unchanged.
     * Examples:
     *  foo_@column_bar ->  foo_{@link Column#name()}_bar
     *  foo_@field_bar -> foo_[composition filed name]_bar
     */
    String renaming() default AUTO_REF;

    /**
     * Bind composition columns associated with the one of specified group.
     * For each composition field can be specified several @Column annotations
     * distinguished by the group name. If the groups is specified,
     * only the @Column associated with this group will be used from the composition
     * @see Column#group()
     */
    String[] groups() default {""};

    /**
     * If FALSE, creates composition object only if any column value is not null,
     * otherwise the composition object will be created regardless of the column values,
     * i.e. even if all column values are null
     */
    boolean nullInstace() default true;

    /**
     * Record views
     * @see RecordKitConfig#views()
     */
    String[] views() default {RecordView.ALL_VIEWS};

    /**
     * Columns overriding
     */
    ColumnOverriding[] columnOverriding() default {};
}
