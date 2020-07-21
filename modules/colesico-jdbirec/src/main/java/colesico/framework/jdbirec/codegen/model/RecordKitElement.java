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

package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordKitElement {

    private final String view;
    private final ClassElement recordKitClass;
    private final ClassType recordType;
    private final ClassType extend;
    private final String tableName;
    private Map<String, String> tableAliases = new HashMap<>();

    private final CompositionElement rootComposition;

    public RecordKitElement(String view, ClassElement recordKitClass, ClassType recordType, ClassType extend, String tableName) {
        this.view = view;
        this.recordKitClass = recordKitClass;
        this.recordType = recordType;
        this.extend = extend;
        this.tableName = tableName;
        this.rootComposition = new CompositionElement(this, recordType.asClassElement(), null);
    }

    public CompositionElement getRootComposition() {
        return rootComposition;
    }

    public ClassType getRecordType() {
        return recordType;
    }

    public boolean hasColumn(ColumnElement columnElement) {
        return rootComposition.hasColumn(columnElement);
    }

    public List<ColumnElement> getAllColumns() {
        List<ColumnElement> result = new ArrayList<>();
        rootComposition.collectSubColumns(result);
        return result;
    }

    public String getTableName() {
        return tableName;
    }

    public ClassType getExtend() {
        return extend;
    }

    public String getView() {
        return view;
    }

    public Map<String, String> getTableAliases() {
        return tableAliases;
    }

    public void addTableAlias(String alias, String table) {
        tableAliases.put(alias, table);
    }

    public ClassElement getRecordKitClass() {
        return recordKitClass;
    }
}
