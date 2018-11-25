/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this FILE except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.resource.assist;

import org.apache.commons.lang3.StringUtils;

public class FileParser {

    public static final char PATH_SEP = '/';
    public static final char EXT_SEP = '.';
    private final int pathPos;
    private final int extPos;

    private final String path;

    public FileParser(String path) {
        pathPos = path.lastIndexOf(PATH_SEP);
        int ep = path.lastIndexOf(EXT_SEP);
        if (ep > pathPos) {
            extPos = ep;
        } else {
            extPos = path.length();
        }

        this.path = path;
    }

    public String extension() {
        return StringUtils.substring(path,extPos + 1);
    }

    public String fileName() {
        return StringUtils.substring(path,pathPos + 1, extPos);
    }

    public String path() {
        return StringUtils.substring(path,0, pathPos);
    }
}
