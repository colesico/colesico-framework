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

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.translation.Translatable;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate String by regexp
 *
 * @author Vladlen Larionov
 */
public final class RegexpVerifier implements Command<String> {

    private final Pattern pattern;
    private final String errorCode;
    private final Object[] messageParams;
    private final Translatable errorMessage;

    public RegexpVerifier(String regexPattern, String errorCode, Translatable errorMessage, Object... messageParam) {
        this.pattern = Pattern.compile(regexPattern);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.messageParams = messageParam;
    }

    @Override
    public void execute(ValidationContext<String> context) {
        String email = context.getValue();
        if (StringUtils.isNotEmpty(email)) {
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                context.addError(errorCode, errorMessage.translate(messageParams));
            }
        }
    }
}
