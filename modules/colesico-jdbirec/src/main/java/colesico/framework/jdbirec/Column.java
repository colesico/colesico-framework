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
 * @see RecordKit
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Columns.class)
@Inherited
public @interface Column {

    String AS_FIELD = "@field";
    String AS_COLUMN = "@column";
    String AS_INSERT = "@insertAs";
    String AS_UPDATE = "@updateAs";
    String AS_NOP = "@nop";

    /**
     * Database column name.
     * Default - auto generated column name from field name
     */
    String name() default AS_FIELD;

    /**
     * Column definition for create table sql.
     * If \@nop is specified column will not be included to  create table definition.
     */
    String definition() default AS_NOP;

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
    String insertAs() default AS_FIELD;

    /**
     * Possible values:
     * \@field  - insert field value
     * \@insertAs - the same as insertAs
     * \@nop -  don't update
     */
    String updateAs() default AS_INSERT;

    /**
     * Column value for selecting.
     * \@column - Select column value. Column identified by @Column.name value
     * \@nop -  Do not enlist the column name in the select statement
     * <p>
     * example: selectAs = 'Value:' || @column
     * </p>
     */
    String selectAs() default AS_COLUMN;

    /**
     * Use this field value in {@link AbstRactrecordKit#exportRecord(Object, AbstRactrecordKit.FieldReceiver)} method.
     * If TRUE, field will be persisted to database
     */
    boolean exportable() default true;

    /**
     * Use this field value in {@link AbstRactrecordKit#importRecord(Object, ResultSet)} method.
     * If TRUE, field will be obtained from sql query result set.
     */
    boolean importable() default true;

    /**
     * Column tags
     *
     * @see RecordView#tagFilter()
     */
    String[] tags() default {};
}
