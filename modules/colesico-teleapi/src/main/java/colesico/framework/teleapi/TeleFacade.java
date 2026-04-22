/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

import jakarta.inject.Provider;

/**
 * Unified facade for tele-method invocations.
 *
 * @param <T> Target whose method will be invoked (usually a service)
 * @param <D> Descriptors registry mapping bet
 */
abstract public class TeleFacade<T, D> {

    public static final String TELE_FACADE_SUFFIX = "Facade";
    public static final String TARGET_PROV_FIELD = "targetProvider";
    public static final String DESCRIPTORS_METHOD = "descriptors";

    /**
     * Target provider.
     * Target - this is an object whose method will be invoked.
     * Typically, this is a service object.
     */
    protected final Provider<T> targetProvider;

    public TeleFacade(Provider<T> targetProvider) {
        this.targetProvider = targetProvider;
    }

    /**
     * Descriptors registry of tele-methods implemented in this tele facade.
     * Descriptor is used to resolve target methods that are called with tele-api.
     */
    abstract public D descriptors();
}