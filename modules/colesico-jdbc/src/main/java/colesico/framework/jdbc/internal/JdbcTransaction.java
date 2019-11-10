/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.jdbc.internal;

import colesico.framework.transaction.Tuning;

import java.sql.Connection;

public class JdbcTransaction {

    private Connection connection;
    private Tuning<Connection> tuning;
    private boolean rollbackOnly = false;

    public Connection getConnection() {
        return connection;
    }

    public JdbcTransaction setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public Tuning<Connection> getTuning() {
        return tuning;
    }

    public JdbcTransaction setTuning(Tuning<Connection> tuning) {
        this.tuning = tuning;
        return this;
    }

    public boolean getRollbackOnly() {
        return rollbackOnly;
    }

    public void setRollbackOnly(Boolean rollbackOnly) {
        this.rollbackOnly = rollbackOnly;
    }
}
