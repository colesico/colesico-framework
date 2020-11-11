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

package colesico.framework.test.resource;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.resource.ResourceKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestParamRewriter {

    private Ioc ioc;
    private ResourceKit rkit;

    Logger logger = LoggerFactory.getLogger(TestParamRewriter.class);


    @BeforeClass
    public void init() {
        logger.info("Init resources test");
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        rkit = ioc.instance(ResourceKit.class);
    }

    @Test
    public void test1() {
        String path;

        path = rkit.rewrite("/");
        assertEquals(path, "/");

        path = rkit.rewrite("/home");
        assertEquals(path, "/home");

        path = rkit.rewrite("home");
        assertEquals(path, "home");

        path = rkit.rewrite("$alias/home");
        assertEquals(path, "foo/dummy/home");

        path = rkit.rewrite("/$alias/home");
        assertEquals(path, "/foo/dummy/home");

        path = rkit.rewrite("/$alias/home/");
        assertEquals(path, "/foo/dummy/home/");

        System.out.println(path);


        path = rkit.rewrite("/$alias");
        assertEquals(path, "/foo/dummy");

        path = rkit.rewrite("/$alias/");
        assertEquals(path, "/foo/dummy/");

        path = rkit.rewrite("$alias");
        assertEquals(path, "foo/dummy");
        logger.info("Resource properties test passed");
    }
}
