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

package colesico.framework.teleapi;

import javax.inject.Provider;

/**
 * Unified façade for remote method calls
 *
 * @author Vladlen Larionov
 */
abstract public class TeleFacade<T, D extends TeleDriver,B> {

    public static final String TELE_FACADE_SUFFIX = "Facade";
    public static final String TELEDRIVER_FIELD = "teleDriver";
    public static final String TARGET_PROV_FIELD = "targetProv";
    public static final String GET_LIGATURE_METHOD = "getLigature";

    /**
     * Tele-invocation driver
     */
    protected final D teleDriver;

    /**
     * Target service
     */
    protected final Provider<T> targetProv;

    public TeleFacade(Provider<T> targetProv, D teleDriver) {
        this.teleDriver = teleDriver;
        this.targetProv = targetProv;
    }

    abstract public B getLigature();
}