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
 * Defines record column. Analogue of JPA @Column annotation
 *
 * @see Record
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
    String name() default "@field";

    /**
     * Value mediator to transmit field value to sql format and back
     *
     * @return
     */
    Class<? extends FieldMediator> mediator() default FieldMediator.class;

    /**
     * Column value for insert and update sql.
     * \@field  - insert field value
     * \@update - the same as insertAs
     * \@nop -  no insertion
     *
     * @return
     */
    String insertAs() default "@field";

    /**
     * Possible values:
     * \@field  - insert field value
     * \@insert - the same as insertAs
     * \@nop -  don't update
     *
     * @return
     */
    String updateAs() default "@insert";

    /**
     * Column value for selecting.
     * \@column - select column value
     * \@nop -  don't select
     * <p>
     *
     * @return
     */
    String selectAs() default "@column";

    /**
     * Column definition for create table sql
     *
     * @return
     */
    String definition() default "";

    /**
     * Use this field value in {@link RecordKit#exportRecord(Object, RecordKit.ColumnAssigner)} method
     *
     * @return
     */
    boolean exportable() default true;

    /**
     * Use this field value in {@link RecordKit#importRecord(Object, ResultSet)} method
     *
     * @return
     */
    boolean importable() default true;

    /**
     * Indicates that the column is not belongs to the record in it is declared and can be linked
     * to another records with @Composition
     *
     * @return
     */
    boolean virtual() default false;

    /**
     * @see Record#profiles()
     * @return
     */
    String[] profiles() default {};
}
