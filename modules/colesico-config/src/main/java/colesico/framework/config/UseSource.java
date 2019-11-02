/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.config;

import java.lang.annotation.*;

/**
 * Configuration source definition for configuration bean.
 * <p>
 * Configuration source contains a configuration values.
 * For example it can be a .properties file or a .yaml file or even sql data base,
 * or remote json resource accessible by http
 * <p>
 *
 * @author Vladlen Larionov
 * @see SourceValue
 * @see ConfigSource
 * @see Config
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface UseSource {

    /**
     * Config source type. PropertiesSource supported out-of the-box.
     *
     * @see PropertiesSource
     * @return
     */
    Class<? extends ConfigSource> type();

    /**
     * Configuration source connection/configurations params in format paramName1,paramValue1,paramName2,paramValue2.. up to ten pairs.
     *
     * @return
     */
    String[] params() default {};
}