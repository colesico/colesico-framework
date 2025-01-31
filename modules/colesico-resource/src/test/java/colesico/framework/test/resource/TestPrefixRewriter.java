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

package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.resource.ResourceKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestPrefixRewriter {

    private Ioc ioc;
    private ResourceKit resourceKit;

    Logger logger = LoggerFactory.getLogger(TestPrefixRewriter.class);


    @BeforeClass
    public void init() {
        logger.info("Init PrefixRewriter  test");
        ioc = IocBuilder.create().build();
        resourceKit = ioc.instance(ResourceKit.class);
    }

    @Test
    public void test1() {
        String path;

        path = resourceKit.localize("/");
        assertEquals(path, "/");

        path = resourceKit.localize("/home/foo");
        assertEquals(path, "/home/foo");

        path = resourceKit.localize("home/foo");
        assertEquals(path, "home/foo");

        path = resourceKit.localize("alias");
        assertEquals(path, "foo/dummy");

        path = resourceKit.localize("/alias");
        assertEquals(path, "/foo/dummy");

        path = resourceKit.localize("/alias/");
        assertEquals(path, "/foo/dummy/");

        path = resourceKit.localize("alias");
        assertEquals(path, "foo/dummy");

        path = resourceKit.localize("alias/home");
        assertEquals(path, "foo/dummy/home");

        path = resourceKit.localize("/alias/home");
        assertEquals(path, "/foo/dummy/home");

        path = resourceKit.localize("/alias/home/");
        assertEquals(path, "/foo/dummy/home/");

        logger.info("Resource properties test passed");
    }
}
