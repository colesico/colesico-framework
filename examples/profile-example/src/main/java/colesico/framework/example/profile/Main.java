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

package colesico.framework.example.profile;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.teleapi.assist.SimpleDataPort;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class Main {

    public static void main(String[] args) {
        Ioc ioc = IocBuilder.create().build();

        // Provide default data port
        SimpleDataPort dataPort = ioc.instance(SimpleDataPort.class);
        dataPort.provide();

        Locale.setDefault(Locale.of("ru"));

        AppService srv = ioc.instance(AppService.class);
        System.out.println("Profile = " + srv.getProfile());

        srv.setLocale(Locale.of("en"));
        System.out.println("Profile/en = " + srv.getProfile());

        srv.setTimezone(TimeZone.getTimeZone("UTC"));
        System.out.println("Timezone/en = " + srv.getProfile());

        System.out.println("DataPort = "+dataPort);
    }
}
