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

import colesico.framework.openapi.codegen.OpenApiModulatorTele;
import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.openapi {

    requires static java.compiler;
    requires static com.squareup.javapoet;

    requires transitive colesico.framework.telehttp;
    requires transitive colesico.framework.restlet;
    requires transitive io.swagger.v3.oas.models;
    requires transitive io.swagger.v3.oas.annotations;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires com.google.gson;


    // API
    exports colesico.framework.telescheme;
    opens colesico.framework.telescheme;

    exports colesico.framework.openapi;
    opens colesico.framework.openapi;

    exports colesico.framework.openapi.assist;
    opens colesico.framework.openadpi.assist;

    // Internal
    exports colesico.framework.openapi.internal to colesico.framework.ioc;

    // Codegen
    exports colesico.framework.openapi.codegen;

    // Resources


    provides Modulator with OpenApiModulatorTele;

}
