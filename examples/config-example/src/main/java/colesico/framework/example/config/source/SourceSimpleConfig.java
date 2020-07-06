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

package colesico.framework.example.config.source;

import colesico.framework.config.Config;
import colesico.framework.config.PropertiesSource;
import colesico.framework.config.SourceOption;
import colesico.framework.config.UseSource;

/**
 * This config by default use {@link PropertiesSource} type and
 * receives values from application.properties
 *
 * To change properties file name or path use {@link SourceOption}
 * @see PropertiesSource
 */
@Config
@UseSource
public class SourceSimpleConfig {

    private String value;

    private String defaultValue ="DefaultValue";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
