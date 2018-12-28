package colesico.framework.fluentjdbc.internal;

import colesico.framework.dba.ConnectionSource;
import org.codejargon.fluentjdbc.api.integration.QueryConnectionReceiver;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleConnectionProvider implements org.codejargon.fluentjdbc.api.integration.ConnectionProvider {

    private final ConnectionSource conProv;

    public SimpleConnectionProvider(ConnectionSource conProv) {
        this.conProv = conProv;
    }

    @Override
    public void provide(QueryConnectionReceiver queryConnectionReceiver) throws SQLException {
        try (Connection conn = conProv.getConnection()) {
            queryConnectionReceiver.receive(conn);
        }
    }
}
