/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.jdbirec.mediators;

import colesico.framework.jdbirec.FieldMediator;
import colesico.framework.jdbirec.RecordKit;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

abstract public class ListMediator<T> implements FieldMediator<List<T>> {

    abstract protected T[] newArray(int size);

    @Override
    public List<T> importField(String columnName, ResultSet rs) throws SQLException {
        Array array = rs.getArray(columnName);
        if (array == null) {
            return null;
        }
        T[] values = (T[]) array.getArray();
        List<T> result = Arrays.asList(values);
        return result;
    }

    @Override
    public void exportField(List<T> value, String fieldName, RecordKit.FieldReceiver fr) {
        fr.set(fieldName, value == null ? null : value.toArray(newArray(value.size())));
    }

}
