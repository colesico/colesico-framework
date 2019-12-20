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
package colesico.framework.http;

import java.util.Date;

/**
 * @author Vladlen Larionov
 */
public final class HttpCookie {

    protected String name;
    protected String value;
    protected Date expires;
    protected String path;
    protected String domain;
    protected Boolean secure;
    protected Boolean httpOnly;
    protected SameSite sameSite;

    public HttpCookie() {
    }

    public HttpCookie(String name, String value) {
        this.name = name;
        this.value = value;
        this.path = "/";
    }


    public String getName() {
        return name;
    }

    public HttpCookie setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public HttpCookie setValue(String value) {
        this.value = value;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HttpCookie setPath(String path) {
        this.path = path;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public HttpCookie setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public Date getExpires() {
        return expires;
    }

    public HttpCookie setExpires(Date expires) {
        this.expires = expires;
        return this;
    }

    public Boolean getSecure() {
        return secure;
    }

    public HttpCookie setSecure(Boolean secure) {
        this.secure = secure;
        return this;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public HttpCookie setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public SameSite getSameSite() {
        return sameSite;
    }

    public void setSameSite(SameSite sameSite) {
        this.sameSite = sameSite;
    }

    @Override
    public String toString() {
        return "HttpCookie{" +
                "name='" + name + '\'' +
                ", model='" + value + '\'' +
                ", expires=" + expires +
                ", path='" + path + '\'' +
                ", domain='" + domain + '\'' +
                ", secure=" + secure +
                ", httpOnly=" + httpOnly +
                ", sameSite=" + sameSite +
                '}';
    }

    public  enum SameSite{
        STRICT, LAX
    }
}
