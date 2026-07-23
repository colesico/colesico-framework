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

package colesico.framework.example.web.staticres;

import colesico.framework.httprouter.Route;
import colesico.framework.weblet.Weblet;
import colesico.framework.webstatic.StaticResource;

@Weblet
public class Resources {

    private final StaticResource staticResource;

    public Resources(StaticResource.Builder staticResourceBuilder) {
        this.staticResource = staticResourceBuilder.resourcesRoot("colesico/framework/example/web/webpub").build();
    }

    // http://localhost:8080/resources/wheel.png
    @Route("*")
    public void get(String routeSuffix, String rewrite) {
        staticResource.send(routeSuffix, "true".equals(rewrite));
    }
}
