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

import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.eventbus {

    requires transitive colesico.framework.ioc;
    requires transitive colesico.framework.service;

    requires org.slf4j;

    requires org.apache.commons.lang3;
    requires java.compiler;
    requires com.squareup.javapoet;

    // classes
    exports colesico.framework.eventbus;
    exports colesico.framework.eventbus.internal to colesico.framework.ioc;

    provides Modulator with colesico.framework.eventbus.codegen.EventBusModulator;
}