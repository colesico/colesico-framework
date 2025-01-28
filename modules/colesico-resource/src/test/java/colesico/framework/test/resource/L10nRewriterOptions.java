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


import colesico.framework.config.Config;
import colesico.framework.resource.rewriters.localization.L10nRewriterOptionsPrototype;

@Config
public class L10nRewriterOptions extends L10nRewriterOptionsPrototype {

    public static final String PATH1 = "root/dir/file{Q}.txt";
    public static final String PATH2 = "root/foo/file{Q}.txt";
    public static final String PATH3 = "root/folder/file{Q}.txt";
    public static final String PATH4 = "root/xxx/file{Q}.txt";
    public static final String PATH5 = "root{Q}/file.txt";

    @Override
    public void configure(Options options) {
        options
                .path(PATH1).qualifiers().language("en").country("GB")
                .path(PATH2).qualifiers().language("en")
                .path(PATH3).qualifiers().country("RU")
                .path(PATH4).qualifiers().language("en").country("RU")
                .path(PATH5).qualifiers().country("RU");

    }
}
