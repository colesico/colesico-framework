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

package colesico.framework.validation;

import colesico.framework.service.ApplicationException;

public class ValidationException extends ApplicationException {

    private final ValidationIssue issue;

    public ValidationException(ValidationIssue issue) {
        super("Validation failed. See issues for details");
        this.issue = issue;
    }

    public ValidationIssue getIssue() {
        return issue;
    }
}
