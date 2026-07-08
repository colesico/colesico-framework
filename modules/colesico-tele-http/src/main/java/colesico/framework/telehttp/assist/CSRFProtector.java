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
import java.util.Set;

import static colesico.framework.assist.StringUtils.isBlank;

/**
 * Modern, adaptive CSRF protector tailored for up-to-date web environments.
 * It provides stateless protection for API requests (e.g., JWT/Bearer tokens) using origin verification,
 * and seamlessly enforces custom request header checks if stateful cookies are present.
 */
@Singleton
public class CSRFProtector {

    public static final String ORIGIN_HEADER = "origin";
    public static final String REFERER_HEADER = "referer";

    // Custom security header for protecting AJAX/Fetch requests without server-side state.
    // Presence of this custom header triggers a CORS preflight check in browsers.
    public static final String CSRF_PROTECTION_HEADER = "x-xsrf-token";
    public static final String CSRF_PROTECTION_COOKIE = "XSRF-TOKEN";

    public static final String REFERER_POLICY_HEADER = "referrer-policy";
    public static final String REFERER_POLICY_HEADER_VALUE = "strict-origin-when-cross-origin";

    // Safe HTTP methods per RFC 7231 that must not alter server state
    private static final Set<String> UNSAFE_METHODS = Set.of("POST", "PUT", "DELETE", "PATCH");


    /**
     * Universal CSRF protection for modern browsers.
     * Secures traditional session-based (cookie) requests and stays transparent for stateless (JWT/Bearer) API requests.
     */
    public void check(HttpRequest request) {
        // 1. Skip safe HTTP methods (GET, HEAD, OPTIONS)
        if (!UNSAFE_METHODS.contains(request.method().name().toUpperCase())) {
            return;
        }

        String requestHost = requestedHostName(request);

        // 2. Strict source verification (Origin / Referer)
        // Prevents unauthorized cross-site form submissions (e.g., application/x-www-form-urlencoded)
        String originHeader = request.headers().get(ORIGIN_HEADER);
        if (originHeader != null) {
            String host = hostFromUrl(originHeader);
            if (!requestHost.equals(host)) {
                throw new RuntimeException("Origin host mismatch: " + host + " -> " + requestHost);
            }
        } else {
            String refererHeader = request.headers().get(REFERER_HEADER);
            if (refererHeader != null) {
                String host = hostFromUrl(refererHeader);
                if (!requestHost.equals(host)) {
                    throw new RuntimeException("Referer host mismatch: " + host + " -> " + requestHost);
                }
            } else {
                throw new RuntimeException("Both Origin and Referer headers are missing");
            }
        }

        // 3. Protection for session-based scenarios (Custom Header Check)
        // If the request contains cookies (web session), the browser must send a custom header.
        // Attackers cannot inject custom headers into cross-domain requests without explicit CORS permissions.
        if (!request.cookies().isEmpty()) {
            String allowedWith = request.headers().get(CSRF_PROTECTION_HEADER);
            if (isBlank(allowedWith)) {
                throw new RuntimeException("Missing security header (X-Allowed-With) for stateful request");
            }
        }
    }

    /**
     * Adds the Referrer Policy header to ensure privacy and retain the Origin header for subsequent requests.
     */
    public String addHeaders(TeleHttpResponse.Builder responseBuilder) {
        responseBuilder
                .header(REFERER_POLICY_HEADER, REFERER_POLICY_HEADER_VALUE);
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
            throw new RuntimeException("Invalid url: " + url);
        }
        return uri.getHost();
    }
}