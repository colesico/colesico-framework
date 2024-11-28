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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is used to customize database columns values to field value transforming and back.
 *
 * @param <F> field class
 */
public interface FieldMediator<F> {

    String IMPORT_METHOD = "importField";
    String EXPORT_METHOD = "exportField";

    /**
     * Import field values from result set and return field value object
     *
     * @param columnName basic column name from @Column.name() annotation
     * @param rs
     * @return
     * @throws SQLException
     */
    F importField(String columnName, ResultSet rs) throws SQLException;

    /**
     * Export field value to the column assigner.
     * Sending process may hit one or more database columns, and fieldValue nested fields
     *
     * @param fieldValue
     * @param fieldName  field name to be bind to sql query param
     * @param fr         field receiver
     */
    void exportField(F fieldValue, String fieldName, AbstRactrecordKit.FieldReceiver fr);

}
