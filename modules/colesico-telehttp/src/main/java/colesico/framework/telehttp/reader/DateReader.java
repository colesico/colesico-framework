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

package colesico.framework.telehttp.reader;

import colesico.framework.teleapi.TeleException;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.OriginFactory;
import colesico.framework.telehttp.OriginTeleReader;
import colesico.framework.telehttp.assist.ISO8601DateParser;
import colesico.framework.telehttp.t9n.Messages;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class DateReader<C extends HttpTRContext> extends OriginTeleReader<Date, C> {
    private final Messages messages;

    @Inject
    public DateReader(OriginFactory originFactory, Messages messages) {
        super(originFactory);
        this.messages = messages;
    }

    @Override
    public Date read(C ctx) {
        try {
            String val = readString(ctx);
            if (StringUtils.isEmpty(val)) {
                return null;
            }
            return ISO8601DateParser.parse(val);
        } catch (Exception ex) {
            throw new TeleException(messages.invalidDateFormat(ctx.getParamName()));
        }
    }
}
