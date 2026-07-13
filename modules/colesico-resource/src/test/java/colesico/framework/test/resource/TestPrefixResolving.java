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
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestPrefixResolving {

    private Ioc ioc;
    private ResourceResolver resourceResolver;

    Logger logger = LoggerFactory.getLogger(TestPrefixResolving.class);

    @BeforeClass
    public void init() {
        logger.info("Init PrefixResolving test");
        ioc = IocBuilder.create().build();
        resourceResolver = ioc.instance(ResourceResolver.class);
    }

    @Test
    public void test1() {
        ioc.instance(TaskScope.class).forTask(() -> {
            String path;

            path = resourceResolver.resolve("/");
            assertEquals(path, "/");

            path = resourceResolver.resolve("/home/foo");
            assertEquals(path, "/home/foo");

            path = resourceResolver.resolve("home/foo");
            assertEquals(path, "home/foo");

            path = resourceResolver.resolve("alias");
            assertEquals(path, "foo/dummy");

            path = resourceResolver.resolve("/alias");
            assertEquals(path, "/foo/dummy");

            path = resourceResolver.resolve("/alias/");
            assertEquals(path, "/foo/dummy/");

            path = resourceResolver.resolve("alias");
            assertEquals(path, "foo/dummy");

            path = resourceResolver.resolve("alias/home");
            assertEquals(path, "foo/dummy/home");

            path = resourceResolver.resolve("/alias/home");
            assertEquals(path, "/foo/dummy/home");

            path = resourceResolver.resolve("/alias/home/");
            assertEquals(path, "/foo/dummy/home/");

            logger.info("Resource properties test passed");
        });
    }
}
