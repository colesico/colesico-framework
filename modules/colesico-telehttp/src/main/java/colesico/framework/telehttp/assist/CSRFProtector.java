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

package colesico.framework.telehttp.assist;

import colesico.framework.http.*;
import colesico.framework.service.ApplicationException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Random;

@Singleton
public class CSRFProtector<V> {

    public static final String ORIGIN_HEADER = "Origin";
    public static final String REFERER_HEADER = "Referer";
    public static final String CSRF_HEADER = "X-CSRF-Token";
    public static final String CSRF_COOKIE = "XSRF-Token";

    protected final CookieFactory cookieFactory;

    private static final Random random = new Random();

    public CSRFProtector(CookieFactory cookieFactory) {
        this.cookieFactory = cookieFactory;
    }

    protected static String getRequestedHostName(HttpRequest request) {
        return request.getHost();
    }

    protected static String getHostFromUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid url:" + url);
        }
        return uri.getHost();
    }

    /**
     * CSRF protection
     */
    public void check(HttpRequest request) {

        // Skip GET requests for referer check
        if (HttpMethod.HTTP_METHOD_GET.equals(request.getRequestMethod())) {
            return;
        }

        // Check ORIGIN Http Header
        String requestHost = getRequestedHostName(request);
        String originHeader = request.getHeaders().get(ORIGIN_HEADER);
        if (originHeader != null) {
            String host = getHostFromUrl(originHeader);
            if (!requestHost.equals(host)) {
                throw new ApplicationException("Origin host mismatch:" + host + "->" + requestHost);
            }
            return;
        }

        // Check REFERER Http Header
        String refererHeader = request.getHeaders().get(REFERER_HEADER);
        if (refererHeader != null) {
            String host = getHostFromUrl(refererHeader);
            if (!requestHost.equals(host)) {
                throw new ApplicationException("Referer host mismatch:" + host + "->" + requestHost);
            }
            return;
        }

        // Check Double submit token
        HttpCookie cookie = request.getCookies().get(CSRF_COOKIE);
        if (cookie == null) {
            return;
        }

        String csrfCookieToken = cookie.getValue();
        String csrfHeaderToken = request.getHeaders().get(CSRF_HEADER);

        if (!StringUtils.equals(csrfCookieToken, csrfHeaderToken)) {
            throw new ApplicationException("CSRF token mismatch:" + csrfCookieToken + " != " + csrfHeaderToken);
        }
    }

    public String sendToken(HttpResponse response) {
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        String tokenStr = Base64.getEncoder().encodeToString(tokenBytes);
        HttpCookie cookie = cookieFactory.create(CSRF_COOKIE, tokenStr);
        response.setCookie(cookie);
        return tokenStr;
    }
}
