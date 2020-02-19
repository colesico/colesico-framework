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
    public void addRewritings(RewritingsDigest digest) {
    }

    /**
     * Allows to add resource localizations
     *
     * @param digest
     */
    public void addLocalizations(LocalizationsDigest digest) {
    }

    public void addProperties(PropertiesDigest digest) {
    }

    public interface PropertiesDigest {
        PropertiesDigest add(String name, String value);
    }

    /**
     * Reciting rules
     */
    public interface RewritingsDigest {
        /**
         * Add rewriting
         * @param originPathPrefix origin path prefix or full path
         * @param targetPathPrefix target path prefix of full path
         */
        RewritingsDigest add(String originPathPrefix, String targetPathPrefix);
    }

    public interface LocalizationsDigest {

        /**
         * Binds possible qualifiers values to specific resource path
         *
         * @param path              resource path to be localized
         * @param qualifiersSpec qualifier values set in format qualName1=val1;qualName2=val2...
         *                          Qualifier values order is unimportant.
         * @see colesico.framework.profile.ProfileConfigPrototype
         */
        LocalizationsDigest add(String path, String... qualifiersSpec);

        default LocalizationsDigest add(Class clazz, String... qualifiersSpec) {
            return add(clazz.getCanonicalName().replace('.', '/'), qualifiersSpec);
        }
    }
}
