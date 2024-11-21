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
import java.sql.ResultSet;

/**
 * Defines record or composition column.
 * Analogue of JPA @Column annotation.
 * A record or composition field may be marked with multiple
 * \@Column annotations, one of which is an actual and the rest mast be virtual
 *
 * @see RecordKitConfig
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Columns.class)
@Inherited
public @interface Column {

    String FIELD_REF = "@field";
    String COLUMN_REF = "@column";
    String INSERT_AS_REF = "@insertAs";
    String UPDATE_AS_REF = "@updateAs";
    String NOP_REF = "@nop";

    /**
     * Database column name.
     * \@filed means auto generated name from field value
     */
    String name() default FIELD_REF;

    /**
     * Column definition for create table sql.
     * If \@nop is specified column will not be included to  create table definition.
     */
    String definition() default "";

    /**
     * Value mediator to transmit field value to sql format and back
     */
    Class<? extends FieldMediator> mediator() default FieldMediator.class;

    /**
     * Column value for insert and update sql.
     * \@field  - insert field value
     * \@updateAs - the same as updateAs
     * \@nop -  no insertion
     */
    String insertAs() default FIELD_REF;

    /**
     * Possible values:
     * \@field  - insert field value
     * \@insertAs - the same as insertAs
     * \@nop -  don't update
     */
    String updateAs() default INSERT_AS_REF;

    /**
     * Column value for selecting.
     * <p>
     * \@column - Select column value. Column identified by @Column.name value
     * To specify another column for selection use: @column([name]), where [name] - another column name
     * <p>
     * \@nop -  Don't select
     */
    String selectAs() default COLUMN_REF;

    /**
     * Use this field value in {@link AbstractRecordKit#exportRecord(Object, AbstractRecordKit.FieldReceiver)} method.
     * If TRUE, field will be persisted to database
     */
    boolean exportable() default true;

    /**
     * Use this field value in {@link AbstractRecordKit#importRecord(Object, ResultSet)} method.
     * If TRUE, field will be obtained from sql query result set.
     */
    boolean importable() default true;

    /**
     * Record views
     *
     * @see RecordKitConfig#views()
     */
    String[] views() default {RecordView.ALL_VIEWS};

    /**
     * Group name to which this column is belongs within the composition.
     * @see Composition#groups()
     */
    String group() default "";
}
