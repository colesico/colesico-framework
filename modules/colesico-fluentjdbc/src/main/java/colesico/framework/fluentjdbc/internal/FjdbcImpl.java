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

package colesico.framework.fluentjdbc.internal;

import colesico.framework.fluentjdbc.Fjdbc;
import colesico.framework.fluentjdbc.FjdbcConfig;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Unscoped;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vladlen Larionov
 */
@Unscoped
public class FjdbcImpl implements Fjdbc {

    protected final Logger log = LoggerFactory.getLogger(FjdbcImpl.class);

    protected final FluentJdbc fluentJdbc;

    public FjdbcImpl(@Message FjdbcConfig config) {
        FluentJdbcBuilder builder = new FluentJdbcBuilder();

        // apply contextual options
        config.applyOptions(builder);

        fluentJdbc = builder.build();

        log.info("Fluent JDBC has been created");
    }

    @Override
    public FluentJdbc getFluentJdbc() {
        return fluentJdbc;
    }

    @Override
    public Query getQuery() {
        return fluentJdbc.query();
    }
}
