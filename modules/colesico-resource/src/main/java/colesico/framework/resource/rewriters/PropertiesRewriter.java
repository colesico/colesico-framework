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

package colesico.framework.resource.rewriters;

import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.RewritingPhase;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Substitutes given properties names with its values
 */
public class PropertiesRewriter implements PathRewriter {

    public static final char PROPERTY_PREFIX = '$';
    public static final char PATH_SEPARATOR = '/';

    private final Map<String, String> propertiesMap = new HashMap<>();

    public static PropertiesRewriter of(String... nameValue) {
        PropertiesRewriter rewriter = new PropertiesRewriter();
        for (int i = 0; i < nameValue.length; i = i + 2) {
            rewriter.property(nameValue[i], nameValue[i + 1]);
        }
        return rewriter;
    }

    @Override
    public String rewrite(String path) {
        List<String> pathItems = splitPath(path);
        int n = pathItems.size();
        for (int i = 0; i < n; i++) {
            String pathItem = pathItems.get(i);
            if ((!"".equals(pathItem)) && pathItem.charAt(0) == PROPERTY_PREFIX) {
                pathItem = getValue(pathItem);
            }
            pathItems.set(i, pathItem);
        }
        return StringUtils.join(pathItems, PATH_SEPARATOR);
    }

    @Override
    public RewritingPhase phase() {
        return RewritingPhase.EVALUATE;
    }

    /**
     * Add property
     */
    public PropertiesRewriter property(String name, String value) {
        if (name == null || "".equals(name)) {
            throw new RuntimeException("Property name is empty or null");
        }

        if (value == null || "".equals(value)) {
            throw new RuntimeException("Value is empty or null");
        }

        if (name.charAt(0) != PROPERTY_PREFIX) {
            throw new RuntimeException("Property name '" + name + "' must starts with '" + PROPERTY_PREFIX + "'");
        }

        String oldPath = propertiesMap.put(name, value);
        if (oldPath != null) {
            throw new RuntimeException("Duplicate property: " + name + "=" + value + " (" + oldPath + ")");
        }

        return this;
    }

    public String getValue(String name) {
        String path = propertiesMap.get(name);
        if (path == null) {
            throw new RuntimeException("Undefined parameter: " + name);
        }
        return path;
    }

    private List<String> splitPath(String path) {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == PATH_SEPARATOR) {
                res.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        res.add(sb.toString());
        return res;
    }

    public void dumpProperties(StringWriter writer) {
        writer.write("Evaluated properties:\n");
        for (Map.Entry<String, String> property : propertiesMap.entrySet()) {
            writer.write("    " + property.getKey() + "=" + property.getValue() + "\n");
        }
    }
}
