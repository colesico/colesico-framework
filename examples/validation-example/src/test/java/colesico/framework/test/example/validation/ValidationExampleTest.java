/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.test.example.validation;

import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.example.validation.dto.User;
import colesico.framework.example.validation.validations.UserValidation;
import colesico.framework.example.validation.validations.UserValidatorBuilder;
import colesico.framework.example.validation.validations.ValidationIoclet;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.teleapi.assist.SimpleDataPort;
import colesico.framework.validation.ValidationIssue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ValidationExampleTest {

    private Ioc ioc;
    private DSLValidator<User> userValidator;

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        ioc.instance(SimpleDataPort.class).provide();

        UserValidation userValidation = ioc.instance(UserValidation.class);
        userValidator = userValidation.build();
    }

    @Test
    public void testValidation() throws Exception {

        User user = new User();
        ValidationIssue issue = userValidator.validate(user);
        // assertTrue(StringUtils.contains(logText,MainBean.class.getName()));
    }

}
