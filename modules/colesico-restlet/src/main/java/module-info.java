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

import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.restlet {

    requires static java.compiler;
    requires static com.squareup.javapoet;

    requires transitive colesico.framework.telehttp;
    requires transitive colesico.framework.validation;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires com.google.gson;

    // API
    exports colesico.framework.restlet;
    opens colesico.framework.restlet;
    exports colesico.framework.restlet.teleapi;
    exports colesico.framework.restlet.teleapi.reader;
    exports colesico.framework.restlet.teleapi.writer;
    exports colesico.framework.restlet.teleapi.gson;
    exports colesico.framework.restlet.assist;

    // Internal
    exports colesico.framework.restlet.internal to colesico.framework.ioc;

    // Codegen
    exports colesico.framework.restlet.codegen;

    // Resources
    opens colesico.framework.restlet.t9n to colesico.framework.localization;

    provides Modulator with colesico.framework.restlet.codegen.RestletModulator;

}