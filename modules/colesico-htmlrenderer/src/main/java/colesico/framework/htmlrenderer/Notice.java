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

package colesico.framework.htmlrenderer;

import colesico.framework.service.ApplicationException;
import colesico.framework.validation.ValidationError;
import colesico.framework.validation.ValidationException;
import colesico.framework.validation.ValidationIssue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Vladlen Larionov
 */
public final class Notice {

    public static final String MESSAGE_KEY = "message";
    public static final String VALIDATION_KEY = "validation";

    // Notice type
    private final Type type;

    // Targeted messages
    private final Map<String, String> messages = new HashMap<>();

    public Notice(Type type, ValidationIssue issue) {
        this.type = type;

        buildValidationMessages(issue, null);
    }

    public Notice(Type type, String message) {
        this.type = type;
        this.messages.put(MESSAGE_KEY, message);
    }

    private void buildValidationMessages(ValidationIssue issue, String superKey) {
        String key;
        if (StringUtils.isEmpty(superKey)) {
            key = issue.getSubject();
        } else {
            key = superKey + '.' + issue.getSubject();
        }

        for (Map.Entry<String, ValidationIssue> entry : issue.getSubissues().entrySet()) {
            buildValidationMessages(entry.getValue(), key);
        }

        if (issue.hasErrors()) {
            List<String> msgs = new ArrayList<>();
            for (ValidationError ve : issue.getErrors()) {
                msgs.add(ve.getMessage());
            }
            String msg = StringUtils.join(msgs, "; ");
            String msgKey = StringUtils.isEmpty(key) ? VALIDATION_KEY : key;
            messages.put(msgKey, msg);
        }

    }

    public static Notice info(String message) {
        return new Notice(Type.INFO, message);
    }

    public static Notice error(String message) {
        return new Notice(Type.ERROR, message);
    }

    public static Notice warning(String message) {
        return new Notice(Type.WARNING, message);
    }

    public static Notice success(String message) {
        return new Notice(Type.SUCCESS, message);
    }

    public static Notice error(Exception ex) {
        if (ex instanceof ValidationException) {
            return new Notice(Type.ERROR, ((ValidationException) ex).getIssue());
        }
        if (ex instanceof ApplicationException) {
            return new Notice(Type.ERROR, ex.getMessage());
        }
        return new Notice(Type.ERROR, ExceptionUtils.getRootCauseMessage(ex));
    }

    public boolean isNotEmpty() {
        return !messages.isEmpty();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public Type getType() {
        return type;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getMessage() {
        return messages.get(MESSAGE_KEY);
    }

    public String getInfo() {
        if (type == Type.INFO) {
            return messages.get(MESSAGE_KEY);
        } else {
            return null;
        }
    }

    public String getSuccess() {
        if (type == Type.SUCCESS) {
            return messages.get(MESSAGE_KEY);
        } else {
            return null;
        }
    }

    public String getWarning() {
        if (type == Type.WARNING) {
            return messages.get(MESSAGE_KEY);
        } else {
            return null;
        }
    }

    public String getError() {
        if (type == Type.ERROR) {
            return messages.get(MESSAGE_KEY);
        } else {
            return null;
        }
    }

    public enum Type {
        INFO, SUCCESS, WARNING, ERROR
    }
}
