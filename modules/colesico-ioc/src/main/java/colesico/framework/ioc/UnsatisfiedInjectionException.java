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

package colesico.framework.ioc;

import colesico.framework.ioc.key.Key;

/**
 * Will be throw when the IOC container can not find the factory for given key.
 *
 * @author Vladlen Larionov
 * @see Key
 * @see Ioc
 */
public class UnsatisfiedInjectionException extends IocException {
    public static final String PROVIDER_NOT_FOUND_MSG = "Unsatisfied injection for key '%s'";

    private final Key key;

    public UnsatisfiedInjectionException(Key key) {
        super(String.format(PROVIDER_NOT_FOUND_MSG, key.toString()));
        this.key = key;
    }

    public Key getKey() {
        return key;
    }
}
