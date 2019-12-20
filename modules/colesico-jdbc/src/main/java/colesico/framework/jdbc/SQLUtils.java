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

package colesico.framework.jdbc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SQLUtils {
    public static String loadQueryText(String filePath) {
        if (!filePath.endsWith(".sql")) {
            filePath = filePath + ".sql";
        }
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(filePath)) {
            if (is!=null) {
                InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder text = new StringBuilder();
                for (String line; (line = reader.readLine()) != null; ) {
                    text.append(line).append("\n");
                }
                return text.toString();
            } else {
                throw new RuntimeException("File not found: "+filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
