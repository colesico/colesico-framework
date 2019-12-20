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

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;

public class ColumnElement {

    protected final FieldElement originField;
    protected CompositionElement parentComposition;
    protected final String name;
    protected ClassType mediator;

    protected boolean importable;
    protected boolean exportable;

    protected String insertAs;
    protected String updateAs;
    protected String selectAs;

    protected String definition;

    protected boolean option;

    public ColumnElement(FieldElement originField, String name) {
        if (name == null) {
            throw new RuntimeException("Name is null");
        }
        this.name = name;
        this.originField = originField;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public CompositionElement getParentComposition() {
        return parentComposition;
    }

    public void setParentComposition(CompositionElement parentComposition) {
        this.parentComposition = parentComposition;
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

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ColumnElement that = (ColumnElement) o;

        return name.equals(that.name);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
