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

module colesico.framework.weblet {

    requires static java.compiler;
    requires static com.palantir.javapoet;

    requires transitive colesico.framework.telehttp;

    requires org.slf4j;

    requires org.apache.commons.lang3;
    requires org.apache.commons.io;

    exports colesico.framework.weblet;
    exports colesico.framework.weblet.assist;
    exports colesico.framework.weblet.teleapi;
    exports colesico.framework.weblet.teleapi.origin;
    exports colesico.framework.weblet.teleapi.writer;
    exports colesico.framework.weblet.teleapi.reader;

    exports colesico.framework.weblet.codegen;
    exports colesico.framework.weblet.internal to colesico.framework.ioc;


    provides Modulator with colesico.framework.weblet.codegen.WebletModulator;
}