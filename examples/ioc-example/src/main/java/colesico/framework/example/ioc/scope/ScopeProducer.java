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

package colesico.framework.example.ioc.scope;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.RefreshScoped;
import colesico.framework.ioc.scope.ThreadScoped;

import javax.inject.Singleton;

@Producer
@Produce(Singleton1.class)
@Produce(value = Singleton3.class, scoped = Singleton.class)
@Produce(value = ThreadScoped1.class, scoped = ThreadScoped.class)
@Produce(value = RefreshScoped1.class, scoped = RefreshScoped.class)
public class ScopeProducer {

    @Singleton
    public Singleton2 getSingleton2() {
        return new Singleton2();
    }
}
