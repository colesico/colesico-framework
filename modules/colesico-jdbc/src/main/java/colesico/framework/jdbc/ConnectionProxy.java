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

package colesico.framework.jdbc;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Connection basic proxy
 */
abstract public class ConnectionProxy implements Connection {

    abstract public Connection getPrimaryConnection();

    @Override
    public Statement createStatement() throws SQLException {
        return getPrimaryConnection().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getPrimaryConnection().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return getPrimaryConnection().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return getPrimaryConnection().nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        getPrimaryConnection().setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return getPrimaryConnection().getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        getPrimaryConnection().commit();
    }

    @Override
    public void rollback() throws SQLException {
        getPrimaryConnection().rollback();
    }

    @Override
    public void close() throws SQLException {
        getPrimaryConnection().close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return getPrimaryConnection().isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getPrimaryConnection().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        getPrimaryConnection().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return getPrimaryConnection().isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        getPrimaryConnection().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return getPrimaryConnection().getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        getPrimaryConnection().setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return getPrimaryConnection().getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getPrimaryConnection().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        getPrimaryConnection().clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getPrimaryConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getPrimaryConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getPrimaryConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getPrimaryConnection().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getPrimaryConnection().setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        getPrimaryConnection().setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getPrimaryConnection().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return getPrimaryConnection().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return getPrimaryConnection().setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        getPrimaryConnection().rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getPrimaryConnection().releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getPrimaryConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getPrimaryConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getPrimaryConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return getPrimaryConnection().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return getPrimaryConnection().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return getPrimaryConnection().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return getPrimaryConnection().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return getPrimaryConnection().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return getPrimaryConnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return getPrimaryConnection().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return getPrimaryConnection().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        getPrimaryConnection().setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        getPrimaryConnection().setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return getPrimaryConnection().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return getPrimaryConnection().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getPrimaryConnection().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getPrimaryConnection().createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        getPrimaryConnection().setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return getPrimaryConnection().getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        getPrimaryConnection().abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        getPrimaryConnection().setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return getPrimaryConnection().getNetworkTimeout();
    }

    @Override
    public void beginRequest() throws SQLException {
        getPrimaryConnection().beginRequest();
    }

    @Override
    public void endRequest() throws SQLException {
        getPrimaryConnection().endRequest();
    }

    @Override
    public boolean setShardingKeyIfValid(ShardingKey shardingKey, ShardingKey superShardingKey, int timeout) throws SQLException {
        return getPrimaryConnection().setShardingKeyIfValid(shardingKey, superShardingKey, timeout);
    }

    @Override
    public boolean setShardingKeyIfValid(ShardingKey shardingKey, int timeout) throws SQLException {
        return getPrimaryConnection().setShardingKeyIfValid(shardingKey, timeout);
    }

    @Override
    public void setShardingKey(ShardingKey shardingKey, ShardingKey superShardingKey) throws SQLException {
        getPrimaryConnection().setShardingKey(shardingKey, superShardingKey);
    }

    @Override
    public void setShardingKey(ShardingKey shardingKey) throws SQLException {
        getPrimaryConnection().setShardingKey(shardingKey);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getPrimaryConnection().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getPrimaryConnection().isWrapperFor(iface);
    }
}
