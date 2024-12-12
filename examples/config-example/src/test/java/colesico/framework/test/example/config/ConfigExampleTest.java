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

package colesico.framework.test.example.config;


import colesico.framework.example.config.MainBean;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ConfigExampleTest {
    private Ioc ioc;
    private MainBean service;
    private Logger logger = LoggerFactory.getLogger(ConfigExampleTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.create().build();
        service = ioc.instance(MainBean.class);
    }

    @Test
    public void testSimpleConfig() {
        assertEquals(service.getSimpleConfigValue(), "Simple");
    }

    @Test
    public void testSingleConfig() {
        assertEquals(service.getSingleConfigValue(), "Single0;Single1");
    }

    @Test
    public void testPolyconfig() {
        assertTrue("Poly1;Poly2;".equals(service.getPolyconfigValues()) || "Poly2;Poly1;".equals(service.getPolyconfigValues()));
    }

    @Test
    public void testMessageConfig() {
        assertEquals(service.getMessageConfigValues(), "Message1;Message2");
    }

    @Test
    public void testSourceSimple() {
        assertEquals(service.getSourceSimpleConfigValue(), "AppSourceValue;DefaultValue");
    }

    @Test
    public void testSourceSingle() {
        assertEquals(service.getSourceSingleConfigValue(), "ConfSourceValue");
    }

    @Test
    public void testSourcePrefix() {
        assertEquals(service.getSourcePrefixConfigValue(), "PrefixedConfSourceValue");
        assertEquals(service.getSourcePrefixConfigEmptyValue(), null);
    }

    @Test
    public void testSourceNested() {
        assertEquals(service.getSourceNestedConfigValue(), "NestedValue");
    }

    @Test
    public void testClassedConfigs() {
        assertEquals(service.getClassedConfigValue(), "SingleConfig;PolyConfig");
    }

    @Test
    public void testDefaultConfig() {
        assertEquals(service.getDefaultConfigValue(), "custom");
    }

}
