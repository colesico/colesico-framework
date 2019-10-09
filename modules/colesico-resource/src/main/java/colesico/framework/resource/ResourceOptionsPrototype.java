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

package colesico.framework.resource;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;


/**
 * Allow to specify application resources rewriting rules
 * and localization qualifiers
 *
 * @author Vladlen Larionov
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ResourceOptionsPrototype {

    /**
     * Allows to specify path prefix rewritings: from path prefix -> to path prefix
     *
     * @return
     */
    public void bindRewritings(RewritingsBinder binder) {
    }

    /**
     * Allows to bind  the localization qualifiers to resource path prefix.
     *
     * @param binder
     */
    public void bindQualifiers(QualifiersBinder binder) {
    }

    public void bindProperties(PropertiesBinder binder) {
    }

    public interface PropertiesBinder {
        PropertiesBinder bind(String name, String value);
    }

    public interface RewritingsBinder {
        /**
         * @param originPathPrefix origin path prefix or full path
         * @param targetPathPrefix target path prefix of full path
         */
        RewritingsBinder bind(String originPathPrefix, String targetPathPrefix);
    }

    public interface QualifiersBinder {
        QualifiersBinder bind(String path, String... qualifiersSpec);
    }
}
