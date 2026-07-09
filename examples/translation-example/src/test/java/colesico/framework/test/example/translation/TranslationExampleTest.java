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
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.Profile;
import colesico.framework.teleapi.assist.SimpleDataPort;
import colesico.framework.translation.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TranslationExampleTest {

    private Logger log = LoggerFactory.getLogger(TranslationExampleTest.class);

    private Ioc ioc;

    private SimpleDataPort dataPort;

    @BeforeClass
    public void setUp() {
        log.info("Init translation text");
        ioc = IocBuilder.create().build();
        dataPort = ioc.instance(SimpleDataPort.class);
    }

    @Test(priority = 1)
    public void testFormatter() {
        dataPort.forTask(() -> {
            ProfileMockProducer.en();
            Profile profile = ioc.instance(Profile.class);
            log.info("Bye Profile (en): " + profile);
            assertEquals(profile.locale().getLanguage(), "en");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayBye(), "Bye Anonymous");
        });
    }

    @Test(priority = 2)
    public void testCustomFormatter() {
        dataPort.forTask(() -> {
            ProfileMockProducer.en();
            Profile profile = ioc.instance(Profile.class);
            log.info("Bye Profile (en): " + profile);
            assertEquals(profile.locale().getLanguage(), "en");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayBye(), "Bye Anonymous");

            CustomFormatter cf = (CustomFormatter) ioc.instance(TextFormatter.class);
            assertEquals(cf.getText(), "Bye Anonymous");
        });
    }


    @Test(priority = 3)
    public void testDe() {
        dataPort.forTask(() -> {
            log.info("Test De");
            ProfileMockProducer.de();
            Profile profile = ioc.instance(Profile.class);
            log.info("DE Profile: " + profile);
            assertEquals(profile.locale().getLanguage(), "de");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayHello(), "HI");
        });
    }

    @Test(priority = 4)
    public void testRu() {
        dataPort.forTask(() -> {
            log.info("Test Ru");
            ProfileMockProducer.ru();
            Profile profile = ioc.instance(Profile.class);
            log.info("RU Profile: " + profile);
            assertEquals(profile.locale().getLanguage(), "ru");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayHello(), "Привет");
        });
    }

    @Test(priority = 5)
    public void testEn() {
        dataPort.forTask(() -> {
            log.info("Test En");
            ProfileMockProducer.en();
            Profile profile = ioc.instance(Profile.class);
            log.info("EN Profile: " + profile);
            assertEquals(profile.locale().getLanguage(), "en");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayHello(), "Hello");
        });
    }

    @Test(priority = 6)
    public void testFr() {
        dataPort.forTask(() -> {
            log.info("Test Fr");
            ProfileMockProducer.fr();
            Profile profile = ioc.instance(Profile.class);
            log.info("FR Profile: " + profile);
            assertEquals(profile.locale().getLanguage(), "fr");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayHello(), "Salut");
        });
    }

    @Test(priority = 7)
    public void testEs() {
        dataPort.forTask(() -> {
            log.info("Test Es");
            ProfileMockProducer.es();
            Profile profile = ioc.instance(Profile.class);
            log.info("ES Profile: " + profile);
            assertEquals(profile.locale().getLanguage(), "es");

            AppService srv = ioc.instance(AppService.class);
            assertEquals(srv.sayHello(), "Hola");
        });
    }

}
