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

package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.hikaricp.HikariProperties;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polysupplier;
import colesico.framework.jdbi.AbstractJdbiConfig;
import colesico.framework.jdbi.JdbiOptionsPrototype;

import javax.inject.Inject;
import javax.sql.DataSource;


@Config
public class JdbiConfig extends AbstractJdbiConfig {

    /**
     * Jdbi will use hikari data source configured with hikari.properties file
     * and  optional configurations classed by JdbiConfig  will be applied to jdbi instance
     *
     * @param dataSource
     * @param options
     */
    @Inject
    public JdbiConfig(

        @Classed(HikariProperties.class)
            DataSource dataSource,

        @Classed(JdbiConfig.class)
            Polysupplier<JdbiOptionsPrototype> options) {

        super(dataSource, options);
    }
}
