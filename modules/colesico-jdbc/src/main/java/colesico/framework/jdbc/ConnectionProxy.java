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

package colesico.framework.jdbc;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Connection basic proxy
 */
abstract public class ConnectionProxy implements Connection {

    abstract public Connection primaryConnection();

    @Override
    public Statement createStatement() throws SQLException {
        return primaryConnection().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return primaryConnection().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return primaryConnection().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return primaryConnection().nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        primaryConnection().setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return primaryConnection().getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        primaryConnection().commit();
    }

    @Override
    public void rollback() throws SQLException {
        primaryConnection().rollback();
    }

    @Override
    public void close() throws SQLException {
        primaryConnection().close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return primaryConnection().isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return primaryConnection().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        primaryConnection().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return primaryConnection().isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        primaryConnection().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return primaryConnection().getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        primaryConnection().setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return primaryConnection().getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return primaryConnection().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        primaryConnection().clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return primaryConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return primaryConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return primaryConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return primaryConnection().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        primaryConnection().setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        primaryConnection().setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return primaryConnection().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return primaryConnection().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return primaryConnection().setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        primaryConnection().rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        primaryConnection().releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return primaryConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return primaryConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return primaryConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return primaryConnection().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return primaryConnection().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return primaryConnection().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return primaryConnection().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return primaryConnection().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return primaryConnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return primaryConnection().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return primaryConnection().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        primaryConnection().setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        primaryConnection().setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return primaryConnection().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return primaryConnection().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return primaryConnection().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return primaryConnection().createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        primaryConnection().setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return primaryConnection().getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        primaryConnection().abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        primaryConnection().setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return primaryConnection().getNetworkTimeout();
    }

    @Override
    public void beginRequest() throws SQLException {
        primaryConnection().beginRequest();
    }

    @Override
    public void endRequest() throws SQLException {
        primaryConnection().endRequest();
    }

    @Override
    public boolean setShardingKeyIfValid(ShardingKey shardingKey, ShardingKey superShardingKey, int timeout) throws SQLException {
        return primaryConnection().setShardingKeyIfValid(shardingKey, superShardingKey, timeout);
    }

    @Override
    public boolean setShardingKeyIfValid(ShardingKey shardingKey, int timeout) throws SQLException {
        return primaryConnection().setShardingKeyIfValid(shardingKey, timeout);
    }

    @Override
    public void setShardingKey(ShardingKey shardingKey, ShardingKey superShardingKey) throws SQLException {
        primaryConnection().setShardingKey(shardingKey, superShardingKey);
    }

    @Override
    public void setShardingKey(ShardingKey shardingKey) throws SQLException {
        primaryConnection().setShardingKey(shardingKey);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return primaryConnection().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return primaryConnection().isWrapperFor(iface);
    }
}
