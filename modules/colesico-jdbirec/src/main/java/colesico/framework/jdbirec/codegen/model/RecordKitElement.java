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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecordKitElement {

    /**
     * Origin record kit interface
     */
    private final ClassElement originClass;

    /**
     * Record type
     */
    private final ClassType recordType;

    /**
     * Class that extended by this kit implementation  (default is AbstractRecordKit)
     */
    private final ClassType superclass;


    /**
     * Master table name associated with record of given type
     */
    private final String tableName;

    /**
     * Table aliases to reference within sql queries.
     * Table aliases for the record table and joint records
     */
    private Map<String, String> tableAliases = new HashMap<>();

    /**
     * Records to be used in the joins
     */
    private Map<ClassType, JointRecord> jointRecords = new HashMap<>();

    /**
     * Root compositions derived from record class
     */
    private final Set<RecordElement> records = new HashSet<>();

    public RecordKitElement(ClassElement originClass,
                            ClassType recordType,
                            ClassType superclass,
                            String tableName) {

        this.originClass = originClass;
        this.recordType = recordType;
        this.superclass = superclass;
        this.tableName = tableName;

    }

    public void addRecord(RecordElement rec) {
        if (!records.add(rec)) {
            throw new RuntimeException("Duplicate record view: " + rec.getView());
        }
    }

    public void addJointRecord(JointRecord rec) {
        jointRecords.put(rec.getRecordType(), rec);
    }

    public Set<RecordElement> getRecords() {
        return records;
    }

    public ClassType getRecordType() {
        return recordType;
    }

    public String getTableName() {
        return tableName;
    }

    public ClassType getSuperclass() {
        return superclass;
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
