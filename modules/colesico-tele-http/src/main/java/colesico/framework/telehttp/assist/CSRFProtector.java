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

package colesico.framework.telehttp.assist;

import colesico.framework.http.*;
import colesico.framework.telehttp.response.TeleHttpResponse;

import jakarta.inject.Singleton;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import static colesico.framework.assist.StringUtils.isBlank;

@Singleton
public class CSRFProtector {

    public static final String ORIGIN_HEADER = "Origin";
    public static final String REFERER_HEADER = "Referer";
    public static final String CSRF_HEADER = "X-CSRF-Token";
    public static final String CSRF_COOKIE = "XSRF-Token";

    protected final HttpCookieFactory cookieFactory;

    // SecureRandom is cryptographically strong and must be used instead of java.util.Random
    private static final Random secureRandom = new SecureRandom();
    // Safe HTTP methods per RFC 7231 that must not alter server state
    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");

    public CSRFProtector(HttpCookieFactory cookieFactory) {
        this.cookieFactory = cookieFactory;
    }

    protected static String requestedHostName(HttpRequest request) {
        return request.host();
    }

    protected static String hostFromUrl(String url) {
        if (isBlank(url)) {
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
     * Reinforced CSRF protection algorithm
     */
    public void check(HttpRequest request) {

        // Skip safe GET requests since they must not alter state
        if (SAFE_METHODS.contains(request.method().name().toUpperCase())) {
            return;
        }

        String requestHost = requestedHostName(request);

        // 1. Validate ORIGIN HTTP Header if present
        String originHeader = request.headers().get(ORIGIN_HEADER);
        if (originHeader != null) {
            String host = hostFromUrl(originHeader);
            if (!requestHost.equals(host)) {
                throw new RuntimeException("Origin host mismatch:" + host + "->" + requestHost);
            }
        }
        // 2. Fall back to REFERER HTTP Header if Origin is missing
        else {
            String refererHeader = request.headers().get(REFERER_HEADER);
            if (refererHeader != null) {
                String host = hostFromUrl(refererHeader);
                if (!requestHost.equals(host)) {
                    throw new RuntimeException("Referer host mismatch:" + host + "->" + requestHost);
                }
            } else {
                throw new RuntimeException("Both Origin and Referer headers are missing");
            }
        }

        // 3. Mandatory Doublae Submit Token validation (no more early return bypasses)
        HttpCookie cookie = request.cookies().get(CSRF_COOKIE);
        if (cookie == null) {
            throw new RuntimeException("Missing CSRF cookie");
        }

        String csrfCookieToken = cookie.value();
        String csrfHeaderToken = request.headers().get(CSRF_HEADER);

        if (isBlank(csrfCookieToken) || isBlank(csrfHeaderToken)) {
            throw new RuntimeException("CSRF tokens cannot be blank");
        }

        // Use MessageDigest.isEqual to prevent Timing Attacks
        if (!MessageDigest.isEqual(csrfCookieToken.getBytes(), csrfHeaderToken.getBytes())) {
            throw new RuntimeException("CSRF token mismatch");
        }
    }

    public String sendToken(TeleHttpResponse response) {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        String tokenStr = Base64.getEncoder().encodeToString(tokenBytes);

        HttpCookie cookie = cookieFactory.create(CSRF_COOKIE, tokenStr);

        // Recommended configurations for modern web environments (if your factory supports them):
        // cookie.setHttpOnly(false); // Must be false so SPA JavaScript (React/Vue) can read it
        // cookie.setSecure(true);     // Restrict to HTTPS execution environments
        // cookie.setSameSite("Lax");  // Enable first-layer browser defense mechanism

        response.addCookie(cookie);
        return tokenStr;
    }
}