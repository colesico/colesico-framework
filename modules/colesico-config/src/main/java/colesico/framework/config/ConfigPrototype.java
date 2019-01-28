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
 * Configuration prototype declaration.
 * This annotation should annotate each configuration prototype.
 * <p>
 *
 * @author Vladlen Larionov
 * @see ConfigModel
 * @see Default
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ConfigPrototype {

    /**
     * Defines the configuration model
     *
     * @return
     * @see ConfigModel
     */
    ConfigModel model();

    /**
     * Class in which the configuration will be injected.
     * This value is used for MESSAGE config model to specify the target for that this config is designed.
     * @return
     */
    Class<?> target() default Object.class;
}
