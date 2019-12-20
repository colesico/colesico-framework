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
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Vladlen Larionov
 */
public final class DateFormatVerifier implements Command<String> {

    private final SimpleDateFormat dateFormat;
    private final ValidatorMessages msg;

    public DateFormatVerifier(String format, ValidatorMessages msg) {
        dateFormat = new SimpleDateFormat(format);
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<String> context) {
        try {
            if (StringUtils.isNotEmpty(context.getValue())) {
                dateFormat.parse(context.getValue());
            }
        } catch (ParseException ex) {
            context.addError(DateFormatVerifier.class.getSimpleName(), msg.invalidDateFormat());
        }
    }
}
