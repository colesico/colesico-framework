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


module colesico.framework.hocon {

    // Compile time
    requires static java.compiler;
    requires static com.squareup.javapoet;

    // Inherited in client projects
    requires transitive colesico.framework.ioc;
    requires transitive colesico.framework.config;

    requires org.slf4j;
    requires static org.apache.commons.lang3;
    requires typesafe.config;

    // Exports

    // API
    exports colesico.framework.hocon;
    exports colesico.framework.hocon.internal to colesico.framework.ioc;

}
