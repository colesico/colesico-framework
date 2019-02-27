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
import java.sql.ResultSet;

/**
 * Defines column name
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Columns.class)
public @interface Column {

    /**
     * Column name
     *
     * @return
     */
    String name() default "";

    /**
     * Value converter
     *
     * @return
     */
    Class<? extends FieldConverter> converter() default FieldConverter.class;

    /**
     * Column value for insert and update sql.
     * <p>
     * Default value ':'+name()
     *
     * @return
     */
    String insertAs() default "";

    String updateAs() default "";

    /**
     * Column value for selecting.
     * <p>
     * Default value name()
     *
     * @return
     */
    String selectAs() default "";

    /**
     * Column definition for create table sql
     *
     * @return
     */
    String definition() default "";

    /**
     * Use this field value in {@link RecordKit#exportTo(Object, RecordKit.ValueReceiver)} method
     *
     * @return
     */
    boolean exportable() default true;

    /**
     * Use this field value in {@link RecordKit#importFrom(Object, ResultSet)} method
     *
     * @return
     */
    boolean importable() default true;

    /**
     * Indicates that the column definition is an alternative
     *
     * @return
     */
    boolean option() default false;

}
