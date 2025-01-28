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

package colesico.framework.resource.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.resource.rewriting.PathRewriter;
import colesico.framework.resource.rewriting.ResourceParamOptionsPrototype;
import colesico.framework.resource.rewriting.RewritingPhase;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Replaces parameter names in the path with their values
 * <p>
 * Example:
 * /foo/$param1/$param2/bar/baz for $param1=dummy; $param2=100 will be
 * rewrote to  /foo/dummy/100/bar/baz
 */
@Singleton
public class ParamRewriter implements PathRewriter, ResourceParamOptionsPrototype.Options {

    public static final char PARAM_PREFIX = '$';
    public static final char PATH_SEPARATOR = '/';

    private final Map<String, String> paramsMap = new HashMap<>();

    public ParamRewriter(Polysupplier<ResourceParamOptionsPrototype> configSup) {
        configSup.forEach(conf -> conf.configure(this));
    }

    @Override
    public RewritingPhase phase() {
        return RewritingPhase.EVALUATE;
    }

    /**
     * Add parameter
     */
    @Override
    public ResourceParamOptionsPrototype.Options param(String name, String value) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Property name is empty or null");
        }

        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Value is empty or null");
        }

        if (name.charAt(0) != PARAM_PREFIX) {
            throw new RuntimeException("Property name '" + name + "' must starts with '" + PARAM_PREFIX + "'");
        }

        String oldPath = paramsMap.put(name, value);
        if (oldPath != null) {
            throw new RuntimeException("Duplicate param: " + name + "=" + value + " (" + oldPath + ")");
        }

        return this;
    }

    /**
     * Parameter value
     */
    @Override
    public String getValue(String name) {
        String path = paramsMap.get(name);
        if (path == null) {
            throw new RuntimeException("Undefined parameter: " + name);
        }
        return path;
    }

    @Override
    public String rewrite(String path) {
        List<String> pathItems = splitPath(path);
        int n = pathItems.size();
        for (int i = 0; i < n; i++) {
            String pathItem = pathItems.get(i);
            if ((!"".equals(pathItem)) && pathItem.charAt(0) == PARAM_PREFIX) {
                pathItem = getValue(pathItem);
            }
            pathItems.set(i, pathItem);
        }
        return StringUtils.join(pathItems, PATH_SEPARATOR);
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

    public void dumpParam(StringWriter writer) {
        writer.write("Evaluated parameters:\n");
        for (Map.Entry<String, String> param : paramsMap.entrySet()) {
            writer.write("    " + param.getKey() + "=" + param.getValue() + "\n");
        }
    }
}
