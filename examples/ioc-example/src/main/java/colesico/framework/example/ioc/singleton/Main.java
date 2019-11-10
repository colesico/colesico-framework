/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.ioc.singleton;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        // Build ioc instance. Ioc instance is thread safe
        final Ioc ioc = IocBuilder.forProduction();

        MySingleton1 singleton1 = ioc.instance(MySingleton1.class);
        MySingleton1 singleton11 = ioc.instance(MySingleton1.class);

        singleton1.printCounter();
        singleton11.printCounter();

        MySingleton2 singleton2 = ioc.instance(MySingleton2.class);
        MySingleton2 singleton21 = ioc.instance(MySingleton2.class);

        singleton2.printCounter();
        singleton21.printCounter();

    }
}
