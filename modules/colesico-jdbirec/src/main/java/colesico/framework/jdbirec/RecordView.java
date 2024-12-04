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
 * Record view definition.
 * Represents a different mappings of record to database model.
 * <p>
 * To be able to work within the same record class with different sets of fields of this record,
 * the system of views is used. Each view includes a specific set of record columns or compositions.
 * View name must consist of letters and numbers only.
 * </p>
 * <p>
 * Record view is a root composition.
 * </p>
 *
 * @see Composition
 */
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RecordView {

    String VIEW_DEFAULT = "default";
    String VIEW_FULL = "full";
    String VIEW_BRIEF = "brief";

    /**
     * Record view name.
     * <p>
     */
    String name() default VIEW_DEFAULT;

    /**
     * @see Composition#tagFilter()
     */
    TagFilter tagFilter() default @TagFilter;

    /**
     * @see Composition#renaming()
     */
    String renaming() default "";

    /**
     * @see Composition#columnOverriding()
     */
    ColumnOverriding[] columnOverriding() default {};

}
