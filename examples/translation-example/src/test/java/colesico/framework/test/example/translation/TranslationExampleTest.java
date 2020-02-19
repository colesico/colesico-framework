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

package colesico.framework.test.example.translation;

import colesico.framework.example.translation.AppService;
import colesico.framework.example.translation.ProfileMockProducer;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.profile.Profile;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TranslationExampleTest {


    @Test
    public void testNo() {
        Ioc ioc = IocBuilder.create().build();
        ProfileMockProducer.no();
        Profile profile = ioc.instance(Profile.class);
        assertEquals(profile.getLocale().getLanguage(), "no");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "HI");
    }

    @Test
    public void testRu() {
        Ioc ioc = IocBuilder.create().build();
        ProfileMockProducer.ru();
        Profile profile = ioc.instance(Profile.class);
        assertEquals(profile.getLocale().getLanguage(), "ru");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Привет");
    }

    @Test
    public void testEn() {
        Ioc ioc = IocBuilder.create().build();
        ProfileMockProducer.en();
        Profile profile = ioc.instance(Profile.class);
        assertEquals(profile.getLocale().getLanguage(), "en");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Hello");
    }

    @Test
    public void testFr() {
        Ioc ioc = IocBuilder.create().build();
        ProfileMockProducer.fr();
        Profile profile = ioc.instance(Profile.class);
        assertEquals(profile.getLocale().getLanguage(), "fr");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Salut");
    }

    @Test
    public void testBye() {
        Ioc ioc = IocBuilder.create().build();
        ProfileMockProducer.en();
        Profile profile = ioc.instance(Profile.class);
        assertEquals(profile.getLocale().getLanguage(), "en");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayBye(), "Bye Anonymous");
    }
}
