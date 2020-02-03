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
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpRequest;
import colesico.framework.security.Principal;
import colesico.framework.security.assist.MACUtils;
import colesico.framework.security.teleapi.PrincipalSerializer;
import colesico.framework.weblet.teleapi.PrincipalWebletConfigPrototype;
import colesico.framework.weblet.teleapi.ReaderContext;
import colesico.framework.weblet.teleapi.WebletTeleReader;
import colesico.framework.weblet.teleapi.writer.PrincipalWriter;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Base64;
import java.util.StringTokenizer;

@Singleton
public class PrincipalReader implements WebletTeleReader<Principal> {

    protected final PrincipalWebletConfigPrototype config;
    protected final PrincipalSerializer principalSerializer;
    protected final Provider<HttpContext> httpContextProv;

    public PrincipalReader(PrincipalWebletConfigPrototype config, PrincipalSerializer principalSerializer, Provider<HttpContext> httpContextProv) {
        this.config = config;
        this.principalSerializer = principalSerializer;
        this.httpContextProv = httpContextProv;
    }

    @Override
    public Principal read(ReaderContext context) {
        HttpRequest request = httpContextProv.get().getRequest();

        // Retrieve principal from http header
        String principalValue = request.getHeaders().get(PrincipalWriter.HEADER_NAME);
        if (StringUtils.isBlank(principalValue)) {
            // Retrieve principal from http cookie
            HttpCookie cookie = request.getCookies().get(PrincipalWriter.COOKIE_NAME);
            principalValue = cookie != null ? cookie.getValue() : null;
            if (StringUtils.isBlank(principalValue)) {
                return null;
            }
        }

        StringTokenizer tokenizer = new StringTokenizer(principalValue, PrincipalWriter.ITEM_DELIMITER);
        String principalBase64 = tokenizer.nextToken();
        String signatureBase64 = tokenizer.nextToken();

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] principalBytes = decoder.decode(principalBase64);
        byte[] signatureBytes = decoder.decode(signatureBase64);

        boolean valid = MACUtils.verify(config.getSignatureAlgorithm(), principalBytes, config.getSignatureKey(), signatureBytes);

        if (!valid) {
            throw new SecurityException("Invalid principal signature");
        }

        Principal principal = principalSerializer.deserialize(principalBytes);
        return principal;
    }
}
