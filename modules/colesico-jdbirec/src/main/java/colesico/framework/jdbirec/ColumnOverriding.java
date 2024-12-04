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
 * Column overriding.
 * Defines new spec for composition column which will be included in the record.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ColumnOverriding {

    /**
     * Target composition column name.
     * Nested composition columns can be referenced with comp1.comp2.column
     *
     * @see Column#name()
     */
    String column() default "";

    /**
     * Column name overriding
     */
    String name() default "";

    /**
     * Column definition overriding
     */
    String definition() default "";

    /**
     * Mediator class overriding
     */
    Class<? extends FieldMediator> mediator() default FieldMediator.class;

    // TODO: define other column properties to override
}
