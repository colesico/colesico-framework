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

package colesico.framework.test.dslvalidator;

import colesico.framework.dslvalidator.builder.ValidatorBuilder;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.ioc.Unscoped;
import colesico.framework.validation.Validator;

import javax.inject.Inject;

@Unscoped
public class MyValidatorBuilder extends ValidatorBuilder {

    @Inject
    public MyValidatorBuilder(ValidatorMessages msg) {
        super(msg);
    }

    public Validator<MyDataBean> buildGroup() {
        return program(
            field("ID", v -> v.getId(), required(), interval(0L, 1L, true)),
            field("NAME", v -> v.getName(), required(), length(1, 2)),
            field("VALUE", v -> v.getValue(), required(), length(1, 5))
        );
    }

    public Validator<MyDataBean> buildSubject() {
        return program(
            "MY_BEAN",
            required(),
            field("ID", v -> v.getId(),
                required(),
                interval(0L, 1L, true),
                interval(-1L, -2L, true)
            ),
            field("NAME", v -> v.getName(),
                group(
                    required(),
                    length(0, 1),
                    length(1, 2)
                )
            ),
            field("VALUE", v -> v.getValue(),
                required(),
                length(1, 5)
            )
        );
    }
}
