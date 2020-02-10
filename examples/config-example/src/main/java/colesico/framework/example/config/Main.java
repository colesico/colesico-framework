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


package colesico.framework.example.config;

import colesico.framework.ioc.IocBuilder;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        MainBean service = IocBuilder.forProduction().build().instance(MainBean.class);

        out.println("Value from simple config: "+service.getSimpleConfigValue());
        out.println("Value from single config: "+service.getSingleConfigValue());
        out.println("Values from polyvariant configs: "+service.getPolyconfigValues());
        out.println("Values from message configs: "+service.getMessageConfigValues());
        out.println("Value from simple source config: "+service.getSourceSimpleConfigValue());
        out.println("Value from single source config: "+service.getSourceSingleConfigValue());
        out.println("Value from prefixed source config: "+service.getSourcePrefixConfigValue());
        out.println("Value from nested source config: "+service.getSourceNestedConfigValue());
    }
}
