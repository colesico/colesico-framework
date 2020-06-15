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

package colesico.framework.weblet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.router.RouterContext;
import colesico.framework.teleapi.TeleException;
import colesico.framework.weblet.t9n.WebletMessages;
import colesico.framework.weblet.teleapi.WebletTRContext;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.OptionalLong;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class OptionalLongReader extends AbstractReader<OptionalLong> {
    private final WebletMessages messages;

    @Inject
    public OptionalLongReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv, WebletMessages messages) {
        super(routerContextProv, httpContextProv);
        this.messages = messages;
    }

    @Override
    public OptionalLong read(WebletTRContext ctx) {
        try {
            String val = ctx.getString(this.getRouterContext(), this.getHttpRequest());
            if (StringUtils.isBlank(val)) {
                return null;
            }
            if (val.equals("null")) {
                return OptionalLong.empty();
            } else {
                return OptionalLong.of(Long.parseLong(val));
            }
        } catch (Exception var3) {
            throw new TeleException(messages.invalidNumberFormat(ctx.getName()));
        }
    }
}
