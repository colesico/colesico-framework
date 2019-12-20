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

import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract public class RecordKit<R> {

    public static final String EXPORT_METOD = "exportRecord";
    public static final String IMPORT_METHOD = "importRecord";
    public static final String TABLE_NAME_METHOD = "tableName";
    public static final String SQL_INSERT_METHOD = "sqlInsert";
    public static final String SQL_UPDATE_METHOD = "sqlUpdate";
    public static final String SQL_SELECT_METHOD = "sqlSelect";
    public static final String NEW_RECORD_METHOD = "newRecord";

    public static final String RECORD_PARAM = "rec";
    public static final String FIELD_RECEIVER_PARAM = "fr";
    public static final String RESULT_SET_PARAM = "rs";
    public static final String QUALIFICATION_PARAM = "qualification";

    public static final String TABLE_NAME_REF = "@table";

    abstract public void exportRecord(R rec, FieldReceiver fr);

    abstract public R importRecord(R rec, ResultSet rs) throws SQLException;

    /**
     * Return table name
     *
     * @return
     */
    abstract public String tableName();

    /**
     * Return insert sql query text
     *
     * @return
     */
    abstract public String sqlInsert();

    abstract public String sqlUpdate(String qualification);

    abstract public String sqlSelect(String qualification);

    abstract public R newRecord();

    public final String sql(String sqlText) {
        return sqlText.replace(TABLE_NAME_REF, tableName());
    }

    public final String sqlInsert(String qualification) {
        if (qualification != null) {
            return sqlInsert() + " " + qualification;
        } else {
            return sqlInsert();
        }
    }

    public final String sqlDelete(String qualification) {
        return "delete from " + tableName() + (StringUtils.isNotBlank(qualification) ? ' ' + qualification : "");
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
