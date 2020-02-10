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

package colesico.framework.test.example.jdbc;

import colesico.framework.example.jdbc.AppService;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JdbcExampleTest {

    private Ioc ioc;
    private Logger logger = LoggerFactory.getLogger(JdbcExampleTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();
    }

    @Test
    public void testHelloWorld() {
        logger.info("Run JDBC test");
        AppService service = ioc.instance(AppService.class);
        assertEquals(service.readValue(1), "a-value");
    }
}
