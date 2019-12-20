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

package colesico.framework.validation;

import java.io.Serializable;
import java.util.*;

/**
 * Describes a validation issue
 */
public class ValidationIssue implements Serializable {

    /**
     * Validation subject name
     */
    private String subject;

    /**
     * Validation errors list
     */
    private List<ValidationError> errors = new ArrayList<>();

    /**
     * Nested issues if the subject is complex object
     */
    private Map<String, ValidationIssue> subissues = new HashMap<>();

    public ValidationIssue() {
    }

    public ValidationIssue(String subject) {
        this.subject = subject;
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

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public void setSubissues(Map<String, ValidationIssue> subissues) {
        this.subissues = subissues;
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
