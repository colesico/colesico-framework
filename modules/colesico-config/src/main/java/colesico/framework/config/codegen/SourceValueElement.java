/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.FieldElement;

public class SourceValueElement {
    private final FieldElement originField;
    private final String query;

    public SourceValueElement(FieldElement originField, String query) {
        this.originField = originField;
        this.query = query;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public String getQuery() {
        return query;
    }
}
