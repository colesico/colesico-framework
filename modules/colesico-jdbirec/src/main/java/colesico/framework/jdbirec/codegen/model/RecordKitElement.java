/*
 * Copyright © 2014-2024 Vladlen V. Larionov and others as noted.
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

import java.util.HashMap;
import java.util.Map;

public class RecordKitElement {

    /**
     * Origin record kit interface
     */
    private final ClassElement originClass;

    /**
     * Class that extended by this kit implementation  (default is AbstractRecordKit)
     */
    private final ClassType superclass;

    /**
     * Record to be processed
     */
    private RecordElement record;

    /**
     * Table aliases to reference within sql queries.
     * Table aliases for the record table and joint records
     */
    private final Map<String, String> tableAliases = new HashMap<>();

    /**
     * Records to be used in the joins
     */
    private final Map<ClassType, JointRecord> jointRecords = new HashMap<>();

    public RecordKitElement(ClassElement originClass, ClassType superclass) {
        this.originClass = originClass;
        this.superclass = superclass;
    }

    public void addJointRecord(JointRecord rec) {
        jointRecords.put(rec.getRecordType(), rec);
    }

    public void addTableAlias(String alias, String table) {
        tableAliases.put(alias, table);
    }

    public void setRecord(RecordElement record) {
        this.record = record;
    }

    public ClassElement getOriginClass() {
        return originClass;
    }

    public ClassType getSuperclass() {
        return superclass;
    }

    public RecordElement getRecord() {
        return record;
    }

    public Map<String, String> getTableAliases() {
        return tableAliases;
    }

    public Map<ClassType, JointRecord> getJointRecords() {
        return jointRecords;
    }
}
