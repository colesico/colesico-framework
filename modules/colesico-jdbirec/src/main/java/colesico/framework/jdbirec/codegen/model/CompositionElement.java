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

import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.Composition;

import java.util.Set;

/**
 * Composition model
 */
public class CompositionElement extends ContainerElement {

    /**
     * Composition parent container
     */
    private ContainerElement container;

    /**
     * Composition field in the parent container
     */
    private final FieldElement field;

    /**
     * @see Composition#name()
     */
    private final String name;

    /**
     * @see Composition#tags()
     */
    protected final Set<String> tags;


    /**
     * @see Composition#nullInstace()
     */
    private boolean nullInstance = true;
    /**
     * Composition table name.
     * This is for joint records.
     */
    private String tableName;

    public CompositionElement(RecordKitElement recordKit, FieldElement field, String name, Set<String> tags) {
        super(recordKit, field.asClassType());
        this.field = field;
        this.name = name;
        this.tags = tags;
    }

    public void setContainer(ContainerElement container) {
        this.container = container;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setNullInstance(boolean nullInstance) {
        this.nullInstance = nullInstance;
    }

    public ContainerElement getContainer() {
        return container;
    }

    public FieldElement getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public boolean isNullInstance() {
        return nullInstance;
    }


    public String getTableName() {
        return tableName;
    }


    @Override
    public String toString() {
        return "CompositionElement{" +
                "type=" + type +
                ", field=" + field +
                '}';
    }
}
