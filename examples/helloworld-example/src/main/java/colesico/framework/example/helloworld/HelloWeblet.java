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

package colesico.framework.example.helloworld;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.Authentication;
import colesico.framework.telehttp.authentication.HttpBasic;
import colesico.framework.telehttp.response.Responses;
import colesico.framework.telehttp.response.ValueResponse;
import colesico.framework.weblet.Weblet;
import jakarta.inject.Provider;

@Weblet
public class HelloWeblet {

    public static final String SAY_HELLO_TEXT = "Hello World!";
    public static final String SAY_PRIVET_TEXT = "Привет, ";

    private final Provider<Identity> identity;

    public HelloWeblet(Provider<Identity> identity) {
        this.identity = identity;
    }

    // Browse the url: http://localhost:8080/hello-weblet/say-hello
    public ValueResponse<String> sayHello() {
        return Responses.textHtml(SAY_HELLO_TEXT);
    }

    // Browse the url: http://localhost:8080/hello-weblet/privet?name=Татьяна
    public ValueResponse<String> privet(String name) {
        return Responses.textPlain(SAY_PRIVET_TEXT + name);
    }

    // Browse the url: http://localhost:8080/hello-weblet/secured
    // Use admin/secret to  authenticate
    @Authentication(HttpBasic.class)
    public String secured() {
        return (String) identity.get().id();
    }
}
