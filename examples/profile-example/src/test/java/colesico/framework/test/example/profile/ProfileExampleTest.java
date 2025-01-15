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

package colesico.framework.test.example.profile;

import colesico.framework.example.profile.AppService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.teleapi.assist.SimpleDataPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class ProfileExampleTest {

    private Ioc ioc;
    private Logger logger = LoggerFactory.getLogger(ProfileExampleTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();

        // Provide default data port
        SimpleDataPort dataPort = ioc.instance(SimpleDataPort.class);
        dataPort.provide();

        Locale.setDefault(Locale.of("ru"));
    }

    @Test
    public void testGetProfile() {
        logger.info("Get profile");
        AppService service = ioc.instance(AppService.class);
        assertNotNull(service.getProfile());
    }

    @Test
    public void testSetProfile() {
        logger.info("Set profile");
        AppService service = ioc.instance(AppService.class);
        service.setLocale(Locale.of("en"));
        assertEquals(service.getProfile().getLocale().toLanguageTag(), "en");
    }

    @Test
    public void testCustomProfile() {
        logger.info("Custom profile");
        AppService service = ioc.instance(AppService.class);
        service.setTimezone(TimeZone.getTimeZone("UTC"));
        assertEquals(service.getProfile().getTimeZone().getID(), "UTC");
    }
}
