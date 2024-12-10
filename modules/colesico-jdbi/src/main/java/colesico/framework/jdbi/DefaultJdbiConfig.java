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

package colesico.framework.jdbi;

import colesico.framework.config.Config;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Polysupplier;

import javax.inject.Inject;
import javax.sql.DataSource;

import static colesico.framework.ioc.conditional.Substitution.STUB;

/**
 * Default jdbi config.
 */
@Config
@Substitute(STUB)
public final class DefaultJdbiConfig extends AbstractJdbiConfig {

    @Inject
    public DefaultJdbiConfig(
            // Jdbi will use default data source
            DataSource dataSource,

            // Optional configurations will be applied to the jdbi instance.
            Polysupplier<JdbiOptionsPrototype> options) {

        super(dataSource, options);
    }
}
