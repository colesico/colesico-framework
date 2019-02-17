/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

abstract public class TeleVarElement {

    protected TeleMethodElement parentTeleMethod;
    protected TeleVarElement parentVariable;
    protected final VarElement originVariable;

    public TeleVarElement(VarElement originVariable) {
        this.originVariable = originVariable;
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

    @Override
    public String toString() {
        return "TeleVarElement{" +
                "parentTeleMethod=" + parentTeleMethod +
                ", parentVariable=" + parentVariable +
                ", originVariable=" + originVariable +
                '}';
    }
}
