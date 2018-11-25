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


import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Polysupplier;

/**
 * Configuration models
 */
public enum ConfigModel {

    /**
     * Single configuration instance per application (per IoC container).
     * The configuration in this model may be obtained from injector by configuration prototype class name.
     *
     * @see ConfigPrototype
     */
    SINGLE,

    /**
     * Multiple configurations instances per application .
     * The configuration in this model may be obtained from injector by Polyprovider of configuration prototype class name
     *
     * @see ConfigPrototype
     * @see Polysupplier
     */
    POLYVARIANT,

    /**
     * Configuration of tis model is not used directly via injection.
     * To get this configuration the bean should receive configuration instance via IOC Message.
     * The client can inject that  bean by specifying the @Classed qualifier with the configuration implementation class
     *
     * @see Classed
     * @see Message
     * @see Default
     */
    MESSAGE
}
