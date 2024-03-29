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

package colesico.framework.example.jdbc;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {
        AppService srv = IocBuilder.create().build().instance(AppService.class);
        System.out.println("Value from default DB = " + srv.readValue(1));
        System.out.println("Value from extra DB = " + srv.readExtraValue(2));
    }
}
