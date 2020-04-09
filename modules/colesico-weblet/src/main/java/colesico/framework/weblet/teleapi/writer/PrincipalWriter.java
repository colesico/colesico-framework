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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
import colesico.framework.security.Principal;
import colesico.framework.security.assist.MACUtils;
import colesico.framework.security.teleapi.PrincipalSerializer;
import colesico.framework.weblet.teleapi.PrincipalWebletConfigPrototype;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletTDWContext;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Base64;
import java.util.Calendar;


@Singleton
public class PrincipalWriter implements WebletTeleWriter<Principal> {

    public static final String COOKIE_NAME = "principal";
    public static final String HEADER_NAME = "Principal";
    public static final String ITEM_DELIMITER = ":";


    protected final PrincipalWebletConfigPrototype config;
    protected final Provider<HttpContext> httpContextProv;
    protected final PrincipalSerializer principalSerializer;

    public PrincipalWriter(PrincipalWebletConfigPrototype config, Provider<HttpContext> httpContextProv, PrincipalSerializer principalSerializer) {
        this.config = config;
        this.httpContextProv = httpContextProv;
        this.principalSerializer = principalSerializer;
    }

    @Override
    public void write(Principal principal, WebletTDWContext context) {
        String principalValue;
        Calendar expires = Calendar.getInstance();

        // Serialize, sign and encode

        if (principal != null) {

            byte[] principalBytes = principalSerializer.serialize(principal);
            byte[] signatureBytes = MACUtils.sign(config.getSignatureAlgorithm(), principalBytes, config.getSignatureKey());

            Base64.Encoder encoder = Base64.getEncoder();
            String principalBas64 = encoder.encodeToString(principalBytes);
            String signatureBase64 = encoder.encodeToString(signatureBytes);

            StringBuilder sb = new StringBuilder();
            sb.append(principalBas64).append(ITEM_DELIMITER).append(signatureBase64);
            principalValue = sb.toString();
            expires.add(Calendar.DAY_OF_MONTH, config.getCookieValidityDays());
        } else {
            principalValue = null;
            expires.add(Calendar.DAY_OF_MONTH, -1);
        }

        // Calc expiring
        HttpCookie cookie = new HttpCookie(COOKIE_NAME, principalValue);

        cookie.setExpires(expires.getTime())
                .setHttpOnly(true)
                //TODO: check for nginx proxing with http ability
                //.setSecure(true)
                .setSameSite(HttpCookie.SameSite.STRICT);

        HttpResponse response = httpContextProv.get().getResponse();
        response.setCookie(cookie);
        response.setHeader(HEADER_NAME, principalValue);
    }
}
