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

import colesico.framework.ioc.Polysupplier;

import javax.sql.DataSource;

/**
 * Default abstract jdbi config
 */
abstract public class AbstractJdbiConfig extends JdbiConfigPrototype {

    protected final DataSource dataSource;
    protected final Polysupplier<JdbiOptionsPrototype> options;

    public AbstractJdbiConfig(DataSource dataSource,
                              Polysupplier<JdbiOptionsPrototype> options) {
        this.dataSource = dataSource;
        this.options = options;
    }

    public AbstractJdbiConfig(DataSource dataSource) {
        this.dataSource = dataSource;
        this.options = null;
    }

    public final DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public final Polysupplier<JdbiOptionsPrototype> getOptions() {
        return options;
    }

}
