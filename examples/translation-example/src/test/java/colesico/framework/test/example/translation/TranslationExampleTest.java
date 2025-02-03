/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.test.example.translation;

import colesico.framework.example.translation.AppService;
import colesico.framework.example.translation.ProfileMockProducer;
import colesico.framework.example.translation.formatter.CustomFormatter;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.profile.Profile;
import colesico.framework.translation.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TranslationExampleTest {

    private Logger log = LoggerFactory.getLogger(TranslationExampleTest.class);

    private Ioc ioc;

    // is used for clear translation bundle cache
    private ThreadScope threadScope;

    @BeforeClass
    public void setUp() {
        log.info("Init translation text");
        ioc = IocBuilder.create().build();
        threadScope = ioc.instance(ThreadScope.class);
    }

    @Test(priority = 1)
    public void testFormatter() {
        threadScope.init();
        ProfileMockProducer.en();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("Bye Profile (en): " + profile);
        assertEquals(profile.getLocale().getLanguage(), "en");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayBye(), "Bye Anonymous");
        threadScope.destroy();
    }

    @Test(priority = 2)
    public void testCustomFormatter() {
        threadScope.init();

        ProfileMockProducer.en();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("Bye Profile (en): " + profile);
        assertEquals(profile.getLocale().getLanguage(), "en");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayBye(), "Bye Anonymous");

        CustomFormatter cf = (CustomFormatter) ioc.instance(TextFormatter.class);
        assertEquals(cf.getText(), "Bye Anonymous");
        threadScope.destroy();
    }


    @Test(priority = 3)
    public void testDe() {

        log.info("Test DE");
        threadScope.init();
        ProfileMockProducer.de();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("DE Profile: " + profile);
        assertEquals(profile.getLocale().getLanguage(), "de");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "HI");
        threadScope.destroy();

    }

    @Test(priority = 4)
    public void testRu() {
        threadScope.init();

        log.info("Test RU");
        ProfileMockProducer.ru();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("RU Profile: " + profile);
        assertEquals(profile.getLocale().getLanguage(), "ru");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Привет");
        threadScope.destroy();
    }

    @Test(priority = 5)
    public void testEn() {
        threadScope.init();

        log.info("Test EN");
        ProfileMockProducer.en();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("EN Profile: " + profile);
        assertEquals(profile.getLocale().getLanguage(), "en");

        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Hello");

        threadScope.destroy();
    }

    @Test(priority = 6)
    public void testFr() {
        threadScope.init();

        ProfileMockProducer.fr();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("FR Profile: " + profile);
        assertEquals(profile.getLocale().getLanguage(), "fr");


        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Salut");
        threadScope.destroy();
    }

    @Test(priority = 7)
    public void testEs() {
        threadScope.init();

        ProfileMockProducer.es();
        Profile profile = ioc.instance(Profile.class);
        System.out.println("ES Profile: " + profile);
        assertEquals(profile.getLocale().getLanguage(), "es");


        AppService srv = ioc.instance(AppService.class);
        assertEquals(srv.sayHello(), "Hola");
        threadScope.destroy();
    }


}
