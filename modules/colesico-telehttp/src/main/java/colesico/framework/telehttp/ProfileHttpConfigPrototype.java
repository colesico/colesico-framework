/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.telehttp;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class ProfileHttpConfigPrototype {

    protected static AttributeConfig DEFAULT_ATTRIBUTE_CONFIG = new AttributeConfig(true, true);

    /**
     * Profile cookie validity days
     */
    public int getCookieValidityDays() {
        return 365;
    }

    public AttributeConfig getAttributeConfig(String attributeName) {
        return DEFAULT_ATTRIBUTE_CONFIG;
    }

    /**
     * Profile attribute configuration
     *
     * @param readable - attribute will not be read from the data port
     * @param writable - will not be written to the data port
     */
    public record AttributeConfig(
            boolean readable,
            boolean writable
    ) {
    }
}
