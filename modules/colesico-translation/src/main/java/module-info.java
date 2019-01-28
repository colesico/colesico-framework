/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */


module colesico.framework.translation {

    // Compile time req.
    requires static java.compiler;
    requires static com.squareup.javapoet;

    // requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires transitive colesico.framework.resource;
    //  requires transitive cache2k.api;


    // Exports

    // API
    exports colesico.framework.translation;
    exports colesico.framework.translation.assist;

    // Internals
    exports colesico.framework.translation.internal to colesico.framework.ioc;


}