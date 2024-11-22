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

import java.util.*;

public class RecordKitElement {

    /**
     * View associated with this kit
     */
    private final RecordViewElement view;

    /**
     * Origin record kit interface
     */
    private final ClassElement originClass;

    /**
     * Record type
     */
    private final ClassType recordType;

    /**
     * Master table name associated with record of given type
     */
    private final String tableName;

    /**
     * Table aliases to reference within sql queries
     */
    private Map<String, String> tableAliases = new HashMap<>();

    /**
     * Records to be used in the joins
     */
    private Map<ClassType, JointRecord> jointRecords = new HashMap<>();

    /**
     * Class that extended by this kit implementation  (default is AbstractRecordKit)
     */
    private ClassType superclass;

    private final CompositionElement rootComposition;

    public RecordKitElement(RecordViewElement view, ClassElement originClass, ClassType recordType, ClassType extend, String tableName) {
        this.view = view;
        this.originClass = originClass;
        this.recordType = recordType;
        this.superclass = extend;
        this.tableName = tableName;
        this.rootComposition = new CompositionElement(this, recordType, null, Set.of());
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

    public void addJointRecord(JointRecord rec) {
        jointRecords.put(rec.getRecordType(), rec);
    }

    public String getTableName() {
        return tableName;
    }

    public ClassType getSuperclass() {
        return superclass;
    }

    public RecordViewElement getView() {
        return view;
    }

    public Map<String, String> getTableAliases() {
        return tableAliases;
    }

    public void addTableAlias(String alias, String table) {
        tableAliases.put(alias, table);
    }

    public ClassElement getOriginClass() {
        return originClass;
    }

    public Map<ClassType, JointRecord> getJointRecords() {
        return jointRecords;
    }
}
