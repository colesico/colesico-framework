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

package colesico.framework.config;

import java.lang.annotation.*;

/**
 * The declaration of the configuration.
 * Each configuration should extend the configuration prototype  and be annotated with this annotation.
 * <p>
 *
 * @author Vladlen Larionov
 * @see ConfigModel
 * @see ConfigPrototype
 * @see DefaultConfig
 * @see UseSource
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface Config {
}

