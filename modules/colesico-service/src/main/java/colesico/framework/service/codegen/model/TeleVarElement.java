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

package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.HashMap;
import java.util.Map;

abstract public class TeleVarElement {

    protected TeleMethodElement parentTeleMethod;
    protected TeleVarElement parentVariable;
    protected final VarElement originVariable;

    /**
     * Indicates that variable has a @LocalParam annotation
     */
    final protected Boolean isLocal;

    /**
     * Element properties.
     * Any values associated with this element
     */
    private Map<Class<?>, Object> properties = new HashMap<>();

    public TeleVarElement(VarElement originVariable, Boolean isLocal) {
        this.originVariable = originVariable;
        this.isLocal = isLocal;
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
    }

    public VarElement getOriginVariable() {
        return originVariable;
    }

    public TeleVarElement getParentVariable() {
        return parentVariable;
    }

    public void setParentVariable(TeleVarElement parentVariable) {
        this.parentVariable = parentVariable;
    }

    public Boolean getIsLocal() {
        return isLocal;
    }

    @Override
    public String toString() {
        return "TeleVarElement{" +
                "parentTeleMethod=" + parentTeleMethod +
                ", parentVariable=" + parentVariable +
                ", originVariable=" + originVariable +
                '}';
    }
}
