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
import colesico.framework.resource.internal.EvaluatingTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestEvaluatingTool {

    private Ioc ioc;
    private EvaluatingTool evaluatingTool;

    Logger logger = LoggerFactory.getLogger(TestEvaluatingTool.class);


    @BeforeClass
    public void init() {
        logger.info("Init test");
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        evaluatingTool = ioc.instance(EvaluatingTool.class);
        evaluatingTool.addProperty("$alias", "foo/dummy");
    }

    @Test
    public void test1() {
        String path;

        path = evaluatingTool.evaluate("/");
        assertEquals(path, "/");

        path = evaluatingTool.evaluate("/home");
        assertEquals(path, "/home");

        path = evaluatingTool.evaluate("home");
        assertEquals(path, "home");

        path = evaluatingTool.evaluate("$alias/home");
        assertEquals(path, "foo/dummy/home");

        path = evaluatingTool.evaluate("/$alias/home");
        assertEquals(path, "/foo/dummy/home");

        path = evaluatingTool.evaluate("/$alias/home/");
        assertEquals(path, "/foo/dummy/home/");

        System.out.println(path);


        path = evaluatingTool.evaluate("/$alias");
        assertEquals(path, "/foo/dummy");

        path = evaluatingTool.evaluate("/$alias/");
        assertEquals(path, "/foo/dummy/");

        path = evaluatingTool.evaluate("$alias");
        assertEquals(path, "foo/dummy");
    }
}
