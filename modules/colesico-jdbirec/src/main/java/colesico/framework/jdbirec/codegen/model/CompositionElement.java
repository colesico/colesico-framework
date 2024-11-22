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
import colesico.framework.jdbirec.RecordView;

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
     * @see Composition#name()
     */
    private String name = "";

    /**
     * @see Composition#tagFilter()
     */
    private String tagFilter = RecordView.ALL_TAGS_FILTER;

    /**
     * @see Composition#renaming()
     */
    private String renaming = "";

    /**
     * Columns list exported from composition class
     *
     * @see Composition#columnOverriding()
     */
    private final List<ColumnOverridingElement> columnOverriding = new ArrayList<>();

    /**
     * @see Composition#nullInstace()
     */
    private boolean nullInstance = true;

    /**
     * @see Composition#tags()
     */
    protected final Set<String> tags;


    private final Set<ColumnElement> columns = new LinkedHashSet<>();

    private final Set<CompositionElement> subCompositions = new LinkedHashSet<>();

    /**
     * Composition table name.
     * This is for joint records.
     */
    private String tableName;

    public CompositionElement(RecordKitElement parentRecordKit,
                              ClassType compositionType,
                              FieldElement compositionField,
                              Set<String> compositionTags) {
        this.parentRecordKit = parentRecordKit;
        this.originType = compositionType;
        this.originField = compositionField;
        this.tags = compositionTags;
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

    public void overrideColumn(ColumnOverridingElement oce) {
        columnOverriding.add(oce);
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

    public List<ColumnOverridingElement> getColumnOverriding() {
        return columnOverriding;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRenaming() {
        return renaming;
    }

    public String getTagFilter() {
        return tagFilter;
    }

    public boolean isNullInstance() {
        return nullInstance;
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

    public void setRenaming(String renaming) {
        this.renaming = renaming;
    }

    public void setTagFilter(String tagFilter) {
        this.tagFilter = tagFilter;
    }

    public void setNullInstance(boolean nullInstance) {
        this.nullInstance = nullInstance;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "CompositionElement{" +
                "originType=" + originType +
                ", name='" + name + '\'' +
                '}';
    }
}
