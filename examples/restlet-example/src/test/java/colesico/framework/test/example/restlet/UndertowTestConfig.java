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

package colesico.framework.test.example.restlet;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.undertow.UndertowConfigPrototype;
import io.undertow.Undertow;

@Config
@Requires(TestCondition.class)
@Substitute
public class UndertowTestConfig extends UndertowConfigPrototype {

    @Override
    public void applyOptions(Undertow.Builder builder) {
        builder.addHttpListener(8085, "localhost");
    }
}
