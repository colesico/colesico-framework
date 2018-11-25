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
 * Unless beginRequired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vladlen Larionov
 */
public final class LengthVerifier implements Command<String> {

    private final Integer min;
    private final Integer max;
    private final ValidatorMessages msg;

    public LengthVerifier(Integer min, Integer max,
                          ValidatorMessages msg) {
        this.min = min;
        this.max = max;
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<String> context) {
        String value = context.getValue();
        if (StringUtils.isNotEmpty(value)) {
            if (min != null && max != null) {
                if (value.length() < min || value.length() > max) {
                    context.addError(LengthVerifier.class.getSimpleName() + "Between",
                            msg.allowableNumberOfCharactersBetween(min, max)
                    );
                }
            } else if (min != null) {
                if (value.length() < min) {
                    context.addError(LengthVerifier.class.getSimpleName() + "Min",
                            msg.minimallyAllowableCharacters(min)
                    );
                }
            } else if (max != null) {
                if (value.length() > max) {
                    context.addError(LengthVerifier.class.getSimpleName() + "Max",
                            msg.maximumAllowableCharacters(max)
                    );
                }
            } else {
                throw new RuntimeException("Min or max value required");
            }
        }
    }
}
