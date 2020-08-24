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
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Default jdbi config.
 * To override this config use {@link  colesico.framework.ioc.conditional.Substitute}
 */
@Config
public final class DefaultJdbiConfig extends AbstractJdbiConfig {

    @Inject
    public DefaultJdbiConfig(

            // Jdbi will use hikaricp data source configured with hikari.properties file
            @Classed(HikariProperties.class) DataSource dataSource,

            // Optional configurations will be applied to the jdbi instance.
            @Classed(DefaultJdbiConfig.class) Polysupplier<JdbiOptionsPrototype> options) {

        super(dataSource, options);
    }
}
