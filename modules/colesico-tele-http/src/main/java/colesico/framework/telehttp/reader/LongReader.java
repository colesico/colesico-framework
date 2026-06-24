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

import colesico.framework.teleapi.TeleException;
import colesico.framework.telehttp.HttpReadOptions;
import colesico.framework.telehttp.origin.OriginFactory;
import colesico.framework.telehttp.t9n.Messages;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import static colesico.framework.assist.StringUtils.isBlank;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class LongReader extends OriginReader<Long, HttpReadOptions> {

    private final Messages messages;

    @Inject
    public LongReader(OriginFactory originFactory, Messages messages) {
        super(originFactory);
        this.messages = messages;
    }

    @Override
    public Long read(Class<Long> baseType, HttpReadOptions options) {
        try {
            String val = readString(options);
            if (isBlank(val)) {
                return null;
            }
            return Long.parseLong(val);
        } catch (Exception ex) {
            throw new TeleException(messages.invalidNumberFormat(options.paramName()));
        }
    }
}
