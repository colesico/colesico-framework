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
 * Record marker  (analogue of JPA @Entity)
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Record {

    /**
     * Table name to bind this record
     *
     * @return
     */
    String table() default "";

    /**
     * To be able to work within the same record with different sets of fields of this record,
     * the system of profile is used. Each profile includes a specific set of record fields.
     * Profile name must consist of letters and numbers only.
     *
     * @return
     */
    String[] profiles() default {};

    /**
     * Base class to be extended with generated record kit
     *
     * @return
     */
    Class<?> extend() default RecordKit.class;
}
