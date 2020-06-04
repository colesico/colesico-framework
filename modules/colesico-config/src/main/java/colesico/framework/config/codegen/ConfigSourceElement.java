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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigSourceElement {

    public static final String BAG_CLASS_SUFFIX = "Bag";

    private final ConfigElement parentConfig;
    private final ClassType driver;
    private final Map<String, String> params;
    private final boolean bindAll;

    private final List<SourceValueElement> sourceValues = new ArrayList<>();

    public ConfigSourceElement(ConfigElement parentConfig, ClassType driver, Map<String, String> params, boolean bindAll) {
        this.parentConfig = parentConfig;
        this.driver = driver;
        this.params = params;
        this.bindAll = bindAll;
    }

    public void addSourceValue(SourceValueElement sv) {
        sourceValues.add(sv);
    }

    public ClassType getDriver() {
        return driver;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<SourceValueElement> getSourceValues() {
        return sourceValues;
    }

    public boolean isBindAll() {
        return bindAll;
    }

    public String getBagClassSimpleName() {
        return parentConfig.getImplementation().getSimpleName() + BAG_CLASS_SUFFIX;
    }

}
