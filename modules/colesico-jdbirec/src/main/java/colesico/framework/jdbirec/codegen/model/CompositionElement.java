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
     * Composition parent container  (Record or another composition)
     */
    private final ContainerElement container;

    /**
     * Composition field in the parent container
     */
    private final FieldElement field;

    /**
     * @see Composition#tags()
     */
    protected final Set<String> tags;

    /**
     * @see Composition#nullInstace()
     */
    private boolean nullInstance = true;

    public CompositionElement(final RecordElement record,
                              final ContainerElement container,
                              final FieldElement field,
                              final String name,
                              final Set<String> tags) {
        super(record, field.asClassType(), name);
        this.container = container;
        this.field = field;
        this.tags = tags;
    }

    public void setNullInstance(boolean nullInstance) {
        this.nullInstance = nullInstance;
    }

    /**
     * Get parent container
     */
    public ContainerElement getContainer() {
        return container;
    }

    public FieldElement getField() {
        return field;
    }

    public Set<String> getTags() {
        return tags;
    }

    public boolean isNullInstance() {
        return nullInstance;
    }

    @Override
    public String toString() {
        return "CompositionElement{" +
                "type=" + type +
                ", field=" + field +
                '}';
    }
}
