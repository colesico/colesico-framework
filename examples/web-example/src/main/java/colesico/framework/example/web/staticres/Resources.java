/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.web.staticres;

import colesico.framework.router.Route;
import colesico.framework.weblet.Weblet;
import colesico.framework.webstatic.StaticContent;

@Weblet
public class Resources {

    private final StaticContent staticResource;

    public Resources(StaticContent.Builder staticResourceBuilder) {
        this.staticResource = staticResourceBuilder
                .resourcesRoot(Resources.class.getPackageName()
                        .replace('.', '/')
                        + "/webpub")
                .build();
    }

    // http://localhost:8080/resources/wheel.png
    @Route("*")
    public void get(String routeSuffix, String l10nMode) {
        staticResource.send(routeSuffix, l10nMode);
    }
}
