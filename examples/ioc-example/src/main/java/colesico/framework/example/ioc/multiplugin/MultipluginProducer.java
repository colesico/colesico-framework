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

package colesico.framework.example.ioc.multiplugin;

import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(Plugin1.class)
@Produce(Plugin2.class)
@Produce(MainBeanMLP.class)
public class MultipluginProducer {

    // @Polyproduce indicates multiple PluginInterface implementations
    @Polyproduce
    public PluginInterface getPlugin1(Plugin1 impl) {
        return impl;
    }

    @Polyproduce
    public PluginInterface getPlugin2(Plugin2 impl) {
        return impl;
    }
}
