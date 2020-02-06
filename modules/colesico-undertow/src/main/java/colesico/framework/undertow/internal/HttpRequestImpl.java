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
package colesico.framework.undertow.internal;

import colesico.framework.http.*;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Vladlen Larionov
 */
public final class HttpRequestImpl implements HttpRequest {

    private final HttpServerExchange exchange;
    private FormData formData = null;

    private Map<String, String> headers = null;
    private Map<String, HttpCookie> cookies = null;
    private HttpValues<String, String> queryParams = null;
    private HttpValues<String, String> postParams = null;
    private HttpValues<String, HttpFile> postFiles = null;

    private InputStream inputStream = null;

    public HttpRequestImpl(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    public HttpServerExchange getExchange() {
        return exchange;
    }

    public FormData getFormData() {
        return formData;
    }

    private Map<String, String> createHeadersMap() {
        Map headersMap = new HashMap<>();
        Collection<HttpString> headerNames = exchange.getRequestHeaders().getHeaderNames();
        for (HttpString headerName : headerNames) {
            headersMap.put(headerName.toString(), exchange.getRequestHeaders().getFirst(headerName));
        }
        return Collections.unmodifiableMap(headersMap);
    }

    private Map<String, HttpCookie> createCookiesMap() {
        Map<String, HttpCookie> cookiesMap = new HashMap<>();
        for (Map.Entry<String, Cookie> e : exchange.getRequestCookies().entrySet()) {
            Cookie cookie = e.getValue();
            HttpCookie httpCookie = new HttpCookie()
                    .setName(cookie.getName())
                    .setValue(cookie.getValue())
                    .setDomain(cookie.getDomain())
                    .setPath(cookie.getPath())
                    .setExpires(cookie.getExpires())
                    .setSecure(cookie.isSecure())
                    .setHttpOnly(cookie.isHttpOnly());
            cookiesMap.put(e.getKey(), httpCookie);
        }
        return Collections.unmodifiableMap(cookiesMap);
    }

    private HttpValues<String, String> createQueryParams() {
        Map<String, MultiValue<String>> params = new HashMap<>();

        for (Map.Entry<String, Deque<String>> e : exchange.getQueryParameters().entrySet()) {
            params.put(e.getKey(), new MultiValue<>(e.getValue()));
        }
        return new HttpValues<>(params);
    }

    private void createPostValues() {
        Map<String, MultiValue<String>> params = new HashMap<>();
        postParams = new HttpValues<>(params);

        Map<String, MultiValue<HttpFile>> files = new HashMap<>();
        postFiles = new HttpValues<>(files);

        if (formData == null) {
            formData = exchange.getAttachment(FormDataParser.FORM_DATA);
        }
        if (formData == null) {
            return;
        }

        for (String paramName : formData) {
            Deque<FormData.FormValue> values = formData.get(paramName);

            List<String> foundParams = new ArrayList<>();
            List<HttpFile> foundFiles = new ArrayList<>();

            for (FormData.FormValue value : values) {
                if (value.isFileItem()) {
                    HeaderValues hv = value.getHeaders().get("Content-Type");
                    String contentType = hv != null ? hv.getFirst() : "";
                    HttpFile httpFile = new HttpFileImpl(value.getFileName(), contentType, value.getFileItem().getFile());
                    foundFiles.add(httpFile);
                } else {
                    foundParams.add(value.getValue());
                }
            }

            if (!foundFiles.isEmpty()) {
                files.put(paramName, new MultiValue<>(foundFiles));
            }

            if (!foundParams.isEmpty()) {
                params.put(paramName, new MultiValue<>(foundParams));
            }

        }
    }


    @Override
    public HttpMethod getRequestMethod() {
        return HttpMethod.of(exchange.getRequestMethod().toString());
    }

    @Override
    public String getRequestScheme() {
        return exchange.getRequestScheme();
    }

    @Override
    public String getQueryString() {
        return exchange.getQueryString();
    }

    @Override
    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = createHeadersMap();
        }
        return headers;
    }

    @Override
    public Map<String, HttpCookie> getCookies() {
        if (cookies == null) {
            cookies = createCookiesMap();
        }
        return cookies;
    }

    @Override
    public HttpValues<String, String> getQueryParameters() {
        if (queryParams == null) {
            queryParams = createQueryParams();
        }
        return queryParams;
    }

    @Override
    public HttpValues<String, String> getPostParameters() {
        if (postParams == null) {
            createPostValues();
        }
        return postParams;
    }

    @Override
    public HttpValues<String, HttpFile> getPostFiles() {
        if (postFiles == null) {
            createPostValues();
        }
        return postFiles;
    }

    @Override
    public String getRequestURI() {
        return StringUtils.substringBefore(exchange.getRequestURI(), "?");
    }

    @Override
    public String getHost() {
        return exchange.getHostName();
    }

    @Override
    public Integer getPort() {
        return exchange.getHostPort();
    }

    @Override
    public InputStream getInputStream() {
        if (inputStream == null) {
            exchange.startBlocking();
            inputStream = exchange.getInputStream();
        }
        return inputStream;
    }

    @Override
    public void dump(Writer out) {
        try {
            out.append(getRequestMethod().getName() + "  " + exchange.getRequestURI() + "\n");
            out.append("scheme: " + getRequestScheme() + "\n");
            out.append("protocol: " + exchange.getProtocol() + "\n");
            out.append("remoteAddr: " + exchange.getSourceAddress() + "\n");
            out.append("remoteHost: " + exchange.getSourceAddress().getHostName() + "\n");
            out.append("serverPort: " + exchange.getDestinationAddress().getPort() + "\n");
            out.append("isSecure:" + exchange.isSecure() + "\n");
            for (HeaderValues header : exchange.getRequestHeaders()) {
                for (String value : header) {
                    out.append("header: " + header.getHeaderName() + "=" + value + "\n");
                }
            }
            Map<String, Cookie> cookies = exchange.getRequestCookies();
            if (cookies != null) {
                for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
                    Cookie cookie = entry.getValue();
                    out.append("cookie: " + cookie.getName() + "=" +
                            cookie.getValue() + "\n");
                }
            }
            final SecurityContext sc = exchange.getSecurityContext();
            if (sc != null) {
                if (sc.isAuthenticated()) {
                    out.append("authType: " + sc.getMechanismName() + "\n");
                    out.append("principle: " + sc.getAuthenticatedAccount().getPrincipal() + "\n");
                } else {
                    out.append("authType: none" + "\n");
                }
            }

            if (inputStream == null) {
                out.append("body:\n");
                exchange.startBlocking();
                inputStream = new BufferedInputStream(exchange.getInputStream());
                inputStream.mark(0);
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                String str;
                while ((str = reader.readLine()) != null) {
                    out.append(str);
                }
                inputStream.reset();
            } else {
                out.append("body: Input stream has already been open\n");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class HttpFileImpl implements HttpFile {
        private final String fileName;
        private final String contentType;
        private final Path filePath;

        public HttpFileImpl(String fileName, String contentType, Path filePath) {
            this.fileName = fileName;
            this.contentType = contentType;
            this.filePath = filePath;
        }

        @Override
        public void release() {
            try {
                boolean res = filePath.toFile().delete();
                if (!res) {
                    throw new RuntimeException("Cant's release uploaded file");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getFileName() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public InputStream getInputStream() {
            try {
                return new FileInputStream(filePath.toFile());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
