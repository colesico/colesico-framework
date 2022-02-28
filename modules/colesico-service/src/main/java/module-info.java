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

import colesico.framework.service.codegen.parser.ServiceProcessor;

module colesico.framework.service {

    // Compile time
    requires static java.compiler;
    requires static com.squareup.javapoet;

    // Inherited in child projects
    requires transitive colesico.framework.teleapi;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // Exports
    exports colesico.framework.service;

    // Code generation
    exports colesico.framework.service.codegen.assist;
    exports colesico.framework.service.codegen.model;
    exports colesico.framework.service.codegen.model.teleapi;
    exports colesico.framework.service.codegen.parser;
    exports colesico.framework.service.codegen.modulator;
    exports colesico.framework.service.codegen.generator;

    provides javax.annotation.processing.Processor with ServiceProcessor;
}