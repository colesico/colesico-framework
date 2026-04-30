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

package colesico.framework.telehttp.readwrite.principal;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.security.assist.MACUtils;

import java.nio.charset.StandardCharsets;

/**
 * Configuration prototype to configure security principal reading/writing
 */
@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class PrincipalHttpConfigPrototype {

    public byte[] signatureKey() {
        return "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
    }

    public String signatureAlgorithm() {
        return MACUtils.HmacSHA256;
    }

    public int cookieValidityDays() {
        return 14;
    }

}
