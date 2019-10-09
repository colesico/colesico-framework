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
 * Specifies that the configuration class field value should be obtained from configuration source, declared by @ConfigSource
 *
 * @author Vladlen Larionov
 * @see UseSource
 * @see ConfigSourceDriver
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface SourceValue {

    /**
     * Query to obtain configuration value from configuration source
     *
     * @return
     */
    String value() default "";
}
