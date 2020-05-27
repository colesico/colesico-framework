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

import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract public class RecordKit<R> {

    public static final String EXPORT_METOD = "exportRecord";
    public static final String IMPORT_METHOD = "importRecord";
    public static final String NEW_RECORD_METHOD = "newRecord";

    public static final String TABLE_NAME_METHOD = "tableName";
    public static final String GET_RECORD_TOKEN_METHOD = "getRecordToken";
    public static final String GET_COLUMNS_TOKEN_METHOD = "getColumnsToken";
    public static final String GET_UPDATES_TOKEN = "getUpdatesToken";
    public static final String GET_VALUES_TOKEN = "getValuesToken";

    public static final String RECORD_PARAM = "rec";
    public static final String FIELD_RECEIVER_PARAM = "fr";
    public static final String RESULT_SET_PARAM = "rs";
    public static final String QUALIFICATION_PARAM = "qualification";

    public static final String TABLE_NAME_REF = "@table";
    public static final String RECORD_REF = "@record";
    public static final String COLUMNS_REF = "@columns";
    public static final String UPDATES_REF = "@updates";
    public static final String VALUES_REF = "@values";

    abstract public void exportRecord(R rec, FieldReceiver fr);

    abstract public R importRecord(R rec, ResultSet rs) throws SQLException;

    abstract public R newRecord();

    /**
     * Return table name
     *
     * @return
     */
    abstract public String tableName();

    /**
     * Select columns and expressions, separated by comma:  column1,column2, expr(column3)...
     * This token for use in select statements
     */
    abstract protected String getRecordToken();

    /**
     * Column names separate with  comma:  column1,column2...
     * This token for use in insert statements
     */
    abstract protected String getColumnsToken();

    /**
     * Param names separated with comma: :param1,:param2...
     * This token for use in insert statements
     */
    abstract protected String getValuesToken();

    /**
     * Column assignments separated with comma: column1 = :param1, column2 = :param2 ...
     * This token for use in update statements
     */
    abstract protected String getUpdatesToken();

    /**
     * Transforms sql text with references (@table, @columns, @updates, @values) to actual sql
     */
    public final String sql(String query) {
        return query
                .replace(TABLE_NAME_REF, tableName())
                .replace(RECORD_REF, getRecordToken())
                .replace(COLUMNS_REF, getColumnsToken())
                .replace(VALUES_REF, getValuesToken())
                .replace(UPDATES_REF, getUpdatesToken());
    }

    public final Map<String, Object> map(R record) {
        final Map<String, Object> result = new HashMap<>();
        exportRecord(record, result::put);
        return result;
    }

    public final RowMapper<R> mapper() {
        return (rs, ctx) -> importRecord(newRecord(), rs);
    }

    @FunctionalInterface
    public interface FieldReceiver {

        String SET_METHOD = "set";
        String FIELD_PARAM = "field";
        String VALUE_PARAM = "value";

        void set(String field, Object value);
    }

}
