/*
 * Copyright © 2014-2026 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://apache.org
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.telehttp.assist;

import colesico.framework.http.*;
import colesico.framework.telehttp.HttpTeleException;
import colesico.framework.telehttp.response.TeleHttpResponse;

import jakarta.inject.Singleton;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static colesico.framework.assist.StringUtils.isBlank;

/**
 * CSRF protector tailored for up-to-date web environments.
 * It relies entirely on strict origin verification (Origin and Referer headers).
 * This approach eliminates the need for token generation or Double Submit Cookie patterns,
 * allowing standard HTML forms to work seamlessly out-of-the-box, provided that
 * main session cookies are properly configured with the 'SameSite=Lax' or 'Strict' attribute.
 */
@Singleton
public class CSRFProtector {

    public static final String ORIGIN_HEADER = "origin";
    public static final String REFERER_HEADER = "referer";

    public static final String REFERER_POLICY_HEADER = "referrer-policy";
    public static final String REFERER_POLICY_HEADER_VALUE = "strict-origin-when-cross-origin";

    // Safe HTTP methods per RFC 7231 that must not alter server state
    private static final Set<String> UNSAFE_METHODS = Set.of("POST", "PUT", "DELETE", "PATCH");


    /**
     * Verifies the source of the request to prevent Cross-Site Request Forgery.
     * Validates that the request originates from the exact same host.
     * Throws a RuntimeException if a mismatch is detected or required headers are missing.
     */
    public void check(HttpRequest request) {
        // Skip safe HTTP methods (GET, HEAD, OPTIONS) as they are idempotent
        if (!UNSAFE_METHODS.contains(request.method().name().toUpperCase())) {
            return;
        }

        String requestHost = requestedHostName(request);
        if ("localhost".equals(requestHost)) {
            return;
        }

        // Strict source verification (Origin / Referer)
        // Browsers automatically set these headers for cross-origin unsafe requests, and they cannot be spoofed via JS.
        String originHeader = request.headers().get(ORIGIN_HEADER);
        if (originHeader != null) {
            String host = hostFromUrl(originHeader);
            if (!requestHost.equals(host)) {
                throw HttpTeleException.of("CSRF Blocked: Origin host mismatch. Expected: " + requestHost + ", Got: " + host, 403);
            }
        } else {
            String refererHeader = request.headers().get(REFERER_HEADER);
            if (refererHeader != null) {
                String host = hostFromUrl(refererHeader);
                if (!requestHost.equals(host)) {
                    throw HttpTeleException.of("CSRF Blocked: Referer host mismatch. Expected: " + requestHost + ", Got: " + host, 403);
                }
            } else {
                // If both headers are missing on an unsafe method, it indicates a direct security violation or a legacy bot.
                throw HttpTeleException.of("CSRF Blocked: Both Origin and Referer headers are missing for an unsafe state-changing request.", 403);
            }
        }
    }

    /**
     * Appends essential security headers to the response builder.
     * Ensures that the browser retains the Origin header while protecting user privacy during cross-origin navigation.
     */
    public TeleHttpResponse.Builder addMetadata(TeleHttpResponse.Builder responseBuilder) {
        responseBuilder
                .header(REFERER_POLICY_HEADER, REFERER_POLICY_HEADER_VALUE);
        return responseBuilder;
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
            throw HttpTeleException.of("Invalid url structure: " + url, 500);
        }
        return uri.getHost();
    }

}
