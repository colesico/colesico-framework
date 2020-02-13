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

import colesico.framework.ioc.codegen.parser.ProducersProcessor;
import colesico.framework.ioc.ioclet.Ioclet;

open module colesico.framework.ioc {

    // Compile time
    requires static java.compiler;
    requires static com.squareup.javapoet;

    // Inherited in client projects
    requires transitive javax.inject;

    requires jdk.compiler;

    //requires slf4j.api;
    requires org.slf4j;

    requires org.apache.commons.lang3;

    // API
    exports colesico.framework.assist;
    exports colesico.framework.assist.codegen;

    exports colesico.framework.ioc;
    exports colesico.framework.ioc.ioclet;
    exports colesico.framework.ioc.key;
    exports colesico.framework.ioc.annotation;
    exports colesico.framework.ioc.exception;

    // Code generation
    exports colesico.framework.ioc.codegen.model;
    exports colesico.framework.ioc.codegen.parser;
    exports colesico.framework.ioc.codegen.generator;
    exports colesico.framework.assist.codegen.model;

    provides javax.annotation.processing.Processor with ProducersProcessor;

    uses Ioclet;
}