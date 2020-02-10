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
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.internal.LocalizingTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestLocalizationTool {

    private Ioc ioc;
    private LocalizingTool localizingTool;
    Logger logger = LoggerFactory.getLogger(TestLocalizationTool.class);

    public static final String PATH1 = "root/dir/file.txt";
    public static final String PATH2 = "root/foo/file.txt";
    public static final String PATH3 = "root/folder/file.txt";


    @BeforeClass
    public void init() {
        logger.info("Init test");
        ioc = IocBuilder.forTests().build();
        localizingTool = ioc.instance(LocalizingTool.class);
    }

    @Test
    public void test1() {
        localizingTool.addQualifiers(PATH1, "L=en;C=GB");
        localizingTool.addQualifiers(PATH2, "L=en");
        localizingTool.addQualifiers(PATH3, "C=RU");

        String lpath = localizingTool.localize(PATH1, ResourceKit.L10NMode.FILE);
        System.out.println("path=" + lpath);
        lpath = localizingTool.localize(PATH2, ResourceKit.L10NMode.FILE);
        System.out.println("path=" + lpath);
        lpath = localizingTool.localize(PATH3, ResourceKit.L10NMode.FILE);
        System.out.println("path=" + lpath);
    }
}
