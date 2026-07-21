/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.jdbi.internal;

import colesico.framework.config.Config;
import colesico.framework.config.DefaultMessage;
import colesico.framework.ioc.conditional.Substitute;

import colesico.framework.jdbi.JdbiConfigPrototype;
import jakarta.inject.Inject;

import javax.sql.DataSource;

import static colesico.framework.ioc.conditional.Substitution.STUB;

/**
 * Default jdbi config.
 */
@Config
@DefaultMessage
@Substitute(STUB)
public final class DefaultJdbiConfig extends JdbiConfigPrototype {

    private final DataSource dataSource;

    @Inject
    public DefaultJdbiConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }
}
