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

package colesico.framework.telehttp.reader;

import colesico.framework.assist.StringUtils;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.origin.OriginFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class CharacterReader extends OriginReader<Character, HttpReadOptions> {

    @Inject
    public CharacterReader(OriginFactory originFactory) {
        super(originFactory);
    }

    @Override
    public Character read(HttpReadOptions options) {
        String str = StringUtils.trim(readString(options));
        return !StringUtils.isBlank(str) ? str.charAt(0) : null;
    }
}
