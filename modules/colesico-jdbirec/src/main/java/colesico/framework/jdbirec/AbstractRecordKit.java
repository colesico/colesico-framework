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

/**
 * Basic record kit class
 */
abstract public class AbstractRecordKit<R> implements RecordKitApi<R> {

    public static final String EXPORT_RECORD_METHOD = "exportRecord";
    public static final String IMPORT_RECORD_METHOD = "importRecord";
    public static final String GET_TABLE_ALIASES_METHOD = "getTableAliases";
    public static final String GET_RECORD_TOKEN_METHOD = "getRecordToken";
    public static final String GET_COLUMNS_TOKEN_METHOD = "getColumnsToken";
    public static final String GET_UPDATES_TOKEN_METHOD = "getUpdatesToken";
    public static final String GET_VALUES_TOKEN_METHOD = "getValuesToken";
    public static final String NEW_RECORD_METHOD = "newRecord";
    public static final String GET_TABLE_NAME_METHOD = "getTableName";

    public static final String RECORD_PARAM = "rec";
    public static final String FIELD_RECEIVER_PARAM = "fr";
    public static final String RESULT_SET_PARAM = "rs";

    abstract protected void exportRecord(R rec, FieldReceiver fr);

    abstract protected R importRecord(R rec, ResultSet rs) throws SQLException;

    /**
     * Returns table aliases to table name mapping
     */
    abstract protected Map<String, String> getTableAliases();

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

    @Override
    public String sql(String query) {

        query = query
                .replace(TABLE_NAME_REF, getTableName())
                .replace(RECORD_REF, getRecordToken())
                .replace(COLUMNS_REF, getColumnsToken())
                .replace(VALUES_REF, getValuesToken())
                .replace(UPDATES_REF, getUpdatesToken());

        Map<String, String> tableAls = getTableAliases();
        if (tableAls != null) {
            for (Map.Entry<String, String> jt : tableAls.entrySet()) {
                String ref = "@" + jt.getKey();
                query = query.replace(ref, jt.getValue());
            }
        }

        return query;
    }

    @Override
    public Map<String, Object> map(R record) {
        final Map<String, Object> result = new HashMap<>();
        exportRecord(record, result::put);
        return result;
    }

    @Override
    public RowMapper<R> mapper() {
        return (rs, ctx) -> importRecord(newRecord(), rs);
    }

}
