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

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.Composition;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Composition model
 */
public class CompositionElement {

    /**
     * Parent kit ref
     */
    private final RecordKitElement parentRecordKit;

    /**
     * Parent composition ref
     */
    private CompositionElement parentComposition;

    /**
     * Composition class type
     */
    private final ClassType originType;

    /**
     * Composition field in the parent composition
     */
    private final FieldElement originField;


    /**
     * Columns list imported from composition class
     *
     * @see Composition#columns()
     */
    private final List<ColumnBindingElement> boundColumns = new ArrayList<>();

    /**
     * @see Composition#keyColumn()
     */
    private String keyColumn;

    private String columnsPrefix = "";

    private final Set<ColumnElement> columns = new LinkedHashSet<>();

    private final Set<CompositionElement> subCompositions = new LinkedHashSet<>();

    /**
     * Composition table name.
     * This is for joint records.
     */
    private String tableName;

    public CompositionElement(RecordKitElement parentRecordKit, ClassType originType, FieldElement originField) {
        this.parentRecordKit = parentRecordKit;
        this.originType = originType;
        this.originField = originField;
    }

    /**
     * Checks that the given column contains in this composition or nested.
     * This used to eliminate column duplication
     */
    public boolean hasColumn(ColumnElement columnElement) {
        if (columns.contains(columnElement)) {
            return true;
        }
        for (CompositionElement sce : subCompositions) {
            if (sce.hasColumn(columnElement)) {
                return true;
            }
        }
        return false;
    }

    public void collectSubColumns(List<ColumnElement> allSubColumns) {
        allSubColumns.addAll(columns);
        for (CompositionElement comp : subCompositions) {
            comp.collectSubColumns(allSubColumns);
        }
    }

    public void addSubComposition(CompositionElement compElm) {
        compElm.setParentComposition(this);
        subCompositions.add(compElm);
    }

    public void addColumn(ColumnElement columnElm) {
        if (parentRecordKit.hasColumn(columnElm)) {
            throw CodegenException.of()
                    .message("Duplicate column '" + columnElm.getName() + "' on field '" +
                            this.getOriginType() + '.' +
                            columnElm.getOriginField().getName() + "'." +
                            " Parent composition: " + (this.getParentComposition() != null ? this.getParentComposition() : "null"))
                    .element(columnElm.getOriginField()).build();
        }
        columns.add(columnElm);
        columnElm.setParentComposition(this);
    }

    public void bindColumn(ColumnBindingElement cb) {
        boundColumns.add(cb);
    }

    public RecordKitElement getParentRecordKit() {
        return parentRecordKit;
    }

    public CompositionElement getParentComposition() {
        return parentComposition;
    }

    public ClassType getOriginType() {
        return originType;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public List<ColumnBindingElement> getBoundColumns() {
        return boundColumns;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public String getColumnsPrefix() {
        return columnsPrefix;
    }

    public Set<ColumnElement> getColumns() {
        return columns;
    }

    public Set<CompositionElement> getSubCompositions() {
        return subCompositions;
    }

    public String getTableName() {
        return tableName;
    }

    public void setParentComposition(CompositionElement parentComposition) {
        this.parentComposition = parentComposition;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public void setColumnsPrefix(String columnsPrefix) {
        this.columnsPrefix = columnsPrefix;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "CompositionElement{" +
                "originClass=" + originType +
                ", originField=" + originField +
                '}';
    }
}
