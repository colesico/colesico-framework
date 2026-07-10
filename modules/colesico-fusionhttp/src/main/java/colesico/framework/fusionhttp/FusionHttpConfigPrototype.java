/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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
package colesico.framework.fusionhttp;


import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import io.fusionauth.http.server.HTTPServer;
import io.fusionauth.http.server.HTTPServerConfiguration;

/**
 * @author Vladlen Larionov
 */
@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class FusionHttpConfigPrototype {

    /**
     * Use this method to set up FusionAuth HTTP server options
     */
    abstract public void applyConfiguration(HTTPServerConfiguration config);

}
