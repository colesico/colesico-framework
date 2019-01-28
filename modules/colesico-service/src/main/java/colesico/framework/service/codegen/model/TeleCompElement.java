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

import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

public class TeleCompElement extends TeleVarElement {

    private final List<TeleVarElement> variables;

    public TeleCompElement(VariableElement originVariable) {
        super(originVariable);
        variables = new ArrayList<>();
    }

    public void addVariable(TeleVarElement var) {
        variables.add(var);
    }

    public List<TeleVarElement> getVariables() {
        return variables;
    }
}