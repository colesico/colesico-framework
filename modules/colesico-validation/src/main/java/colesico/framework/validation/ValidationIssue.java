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

package colesico.framework.validation;

import java.io.Serializable;
import java.util.*;

/**
 *
 */
public class ValidationIssue implements Serializable {

    private final String subject;
    private final List<ValidationError> errors;
    private final Map<String, ValidationIssue> subissues;

    public ValidationIssue(String subject) {
        this.subject = subject;
        this.errors = new ArrayList<>();
        this.subissues = new HashMap<>();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasSubissues() {
        return !subissues.isEmpty();
    }

    public ValidationIssue getSubissue(String subject) {
        return subissues.get(subject);
    }

    public String getSubject() {
        return subject;
    }

    public void addError(ValidationError errorMessage) {
        errors.add(errorMessage);
    }

    public void addSubissue(ValidationIssue subissue) {
        subissues.put(subissue.getSubject(), subissue);
    }

    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public Map<String, ValidationIssue> getSubissues() {
        return Collections.unmodifiableMap(subissues);
    }

    @Override
    public String toString() {
        StringBuilder eb = new StringBuilder();
        for (ValidationError e : errors) {
            eb.append(e.toString()).append(",\n");
        }

        StringBuilder ib = new StringBuilder();
        for (ValidationIssue vi : subissues.values()) {
            ib.append(vi.toString()).append(",\n");
        }

        return "ValidationIssue : {" +
                "  subject: '" + subject + "',\n" +
                "  errors: {\n" +
                eb.toString() +
                "  \n},\n" +
                "  subissues: {\n" +
                ib.toString() +
                "  \n}\n" +
                '}';
    }
}
