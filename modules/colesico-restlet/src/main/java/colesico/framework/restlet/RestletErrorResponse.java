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

package colesico.framework.restlet;


import colesico.framework.validation.ValidationIssue;

import java.io.Serializable;

/**
 * @author Vladlen Larionov
 */
public class RestletErrorResponse implements Serializable {

    protected String uri;
    protected Integer status;
    protected String message;
    protected ValidationIssue issue;

    public RestletErrorResponse() {
    }

    public RestletErrorResponse(String uri, Integer status, String message) {
        this.uri = uri;
        this.status = status;
        this.message = message;
        this.issue = null;
    }

    public RestletErrorResponse(String uri, Integer status, ValidationIssue issue) {
        this.uri = uri;
        this.status = status;
        this.issue = issue;
        this.message = "Validation error occurred. See 'issue' field for details.";
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationIssue getIssue() {
        return issue;
    }

    public void setIssue(ValidationIssue issue) {
        this.issue = issue;
    }
}
