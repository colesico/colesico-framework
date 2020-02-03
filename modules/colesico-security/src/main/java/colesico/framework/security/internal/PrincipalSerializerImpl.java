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

package colesico.framework.security.internal;

import colesico.framework.security.DefaultPrincipal;
import colesico.framework.security.teleapi.PrincipalSerializer;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;

/**
 * Default principal serializer
 */
@Singleton
public class PrincipalSerializerImpl implements PrincipalSerializer<DefaultPrincipal> {

    @Override
    public byte[] serialize(DefaultPrincipal principal) {
        return principal.getId().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public DefaultPrincipal deserialize(byte[] principalBytes) {
        String id = new String(principalBytes, StandardCharsets.UTF_8);
        return new DefaultPrincipal(id);
    }
}
