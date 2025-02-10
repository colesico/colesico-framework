/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.test.dslvalidator;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.builder.AbstractValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.validation.Validator;
import jakarta.inject.Inject;

@Unscoped
public class TestValidatorBuilder extends AbstractValidatorBuilder {

    @Inject
    public TestValidatorBuilder(ValidatorMessages msg) {
        super(msg);
    }

    public Validator<DataBean> buildGroup() {
        return validator(
                field("id", DataBean::getId, required(), interval(0L, 1L, true)),
                field("name", DataBean::getName, required(), length(1, 2)),
                field("value", DataBean::getValue, required(), length(1, 5))
        );
    }

    @SuppressWarnings("unchecked")
    public Validator<DataBean> buildChain() {
        return basicValidator(
                "DataBean",
                mandatory(
                        field("id", DataBean::getId,
                                idValidor(),
                                interval(0L, 1L, true)
                        ),
                        map("name", DataBean::getName,
                                series(
                                        required(),
                                        length(0, 1),
                                        length(1, 2)
                                )
                        ),
                        field("value", DataBean::getValue,
                                required(),
                                length(1, 5)
                        )
                )
        );
    }

    public Command<Long> idValidor() {
        return ctx -> {
            if (ctx.getValue() == null || ctx.getValue() < 0) {
                ctx.addError("InvalidId", "Id value mast be great than 0");
            }
        };
    }
}
