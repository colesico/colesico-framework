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

package colesico.framework.example.ioc.lifecycle;

import colesico.framework.ioc.Message;
import colesico.framework.ioc.PostProduce;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(value = MainBeanLFC.class, postProduce = true)
public class LifecycleProducer {

    // This is post produce listener
    @PostProduce
    public MainBeanLFC postProduce(@Message MainBeanLFC instance) {
        instance.setValue("Value");
        return instance;
    }
}
