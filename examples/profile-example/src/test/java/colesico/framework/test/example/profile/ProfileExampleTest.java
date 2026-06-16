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
import colesico.framework.example.profile.custom.CustomProfile;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.profile.Profile;
import colesico.framework.teleapi.assist.SimpleDataPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.testng.AssertJUnit.assertEquals;

public class ProfileExampleTest {

    private Ioc ioc;
    private Logger logger = LoggerFactory.getLogger(ProfileExampleTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        Locale.setDefault(Locale.of("ru"));
    }

    @Test(priority = 1)
    public void testGetProfile() {
        logger.info("Get profile");
        var dataPort = ioc.instance(SimpleDataPort.class);
        dataPort.forTask(() -> {
            AppService service = ioc.instance(AppService.class);
            assertEquals("ru", service.getProfile().locale().toLanguageTag());
        });
    }

    @Test(priority = 2)
    public void testCommitProfile() {
        logger.info("Commit profile");
        var dataPort = ioc.instance(SimpleDataPort.class);
        dataPort.forTask(() -> {
            AppService service = ioc.instance(AppService.class);
            service.setLocale(Locale.of("en"));
            CustomProfile profile = (CustomProfile) dataPort.values().get(Profile.class);
            assertEquals("en", profile.locale().toLanguageTag());
        });
    }

    @Test(priority = 3)
    public void testCommitCustomProfile() {
        logger.info("Commit custom profile");
        var dataPort = ioc.instance(SimpleDataPort.class);
        dataPort.forTask(() -> {
            AppService service = ioc.instance(AppService.class);
            service.setTimezone(TimeZone.getTimeZone("UTC"));
            CustomProfile profile = (CustomProfile) dataPort.values().get(Profile.class);
            assertEquals("UTC", profile.timeZone().getID());
        });
    }

    @Test(priority = 1)
    public void testProfileListener() {
        logger.info("Profile listener");
        var dataPort = ioc.instance(SimpleDataPort.class);
        dataPort.forTask(() -> {
            AppService service = ioc.instance(AppService.class);
            assertEquals("2.0", service.getProfile().apiVersion());
        });
    }
}
