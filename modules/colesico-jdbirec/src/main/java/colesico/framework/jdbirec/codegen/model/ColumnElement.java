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

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.Column;

import java.util.Objects;
import java.util.Set;

import static colesico.framework.jdbirec.Column.*;

public class ColumnElement {

    /**
     * Column field within container
     */
    protected final FieldElement field;

    /**
     * Column name
     */
    protected final String name;

    /**
     * @see Column#tags()
     */
    protected final Set<String> tags;

    /**
     * Parent container ref
     */
    protected ContainerElement container;

    /**
     * Create column SQL definition
     */
    protected String definition;

    /**
     * Mediator class
     *
     * @see colesico.framework.jdbirec.FieldMediator
     */
    protected ClassType mediator;

    protected String insertAs;
    protected String updateAs;
    protected String selectAs;
    /**
     * Column value can be imported from result set
     *
     * @see Column#importable()
     */
    protected boolean importable = true;
    /**
     * Column value can be exported to prepared statement
     *
     * @see Column#exportable()
     */
    protected boolean exportable = true;

    public ColumnElement(FieldElement field, String name, Set<String> tags) {
        this.field = field;
        this.name = name;
        this.tags = tags;
    }

    public FieldElement getField() {
        return field;
    }

    public ContainerElement getContainer() {
        return container;
    }

    public void setContainer(ContainerElement container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public ClassType getMediator() {
        return mediator;
    }

    public void setMediator(ClassType mediator) {
        this.mediator = mediator;
    }

    public String getInsertAs() {
        return insertAs;
    }

    public void setInsertAs(String insertAs) {
        this.insertAs = insertAs;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean isImportable() {
        return importable;
    }

    public void setImportable(boolean importable) {
        this.importable = importable;
    }

    public boolean isExportable() {
        return exportable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public String getSelectAs() {
        return selectAs;
    }

    public void setSelectAs(String selectAs) {
        this.selectAs = selectAs;
    }

    public String getUpdateAs() {
        return updateAs;
    }

    public void setUpdateAs(String updateAs) {
        this.updateAs = updateAs;
    }

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnElement that = (ColumnElement) o;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return "ColumnElement{" +
                "container=" + container +
                ", field=" + field +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
