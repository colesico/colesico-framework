package colesico.framework.jdbc.internal;

import colesico.framework.transaction.Tuning;

import java.sql.Connection;

public class JdbcTransaction {

    private Connection connection;
    private Tuning<Connection> tuning;

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
}
