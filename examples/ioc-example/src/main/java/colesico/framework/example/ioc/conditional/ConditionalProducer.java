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

package colesico.framework.example.ioc.conditional;

import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(RegularBean.class)
@Produce(TestBean.class)
public class ConditionalProducer {

    @Requires(CustomCondition.class)
    public IBean getRegBean(RegularBean impl) {
        return impl;
    }

    @Requires(TestCondition.class)
    public IBean getTestBean(TestBean impl) {
        return impl;
    }

}
