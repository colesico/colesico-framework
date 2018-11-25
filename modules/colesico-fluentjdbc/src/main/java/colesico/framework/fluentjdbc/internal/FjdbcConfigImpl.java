package colesico.framework.fluentjdbc.internal;

import colesico.framework.dbcpool.DBCPool;
import colesico.framework.fluentjdbc.FjdbcConfig;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.integration.ConnectionProvider;

import javax.inject.Singleton;
import java.sql.Connection;

@Singleton
public class FjdbcConfigImpl extends FjdbcConfig {

    private final DBCPool dbcPool;

    public FjdbcConfigImpl(DBCPool dbcPool) {
        this.dbcPool = dbcPool;
    }

    @Override
    public void applyOptions(FluentJdbcBuilder builder) {
        ConnectionProvider cp = query -> {
            Connection connection = dbcPool.getConnection();
            query.receive(connection);
            connection.close();
        };
        builder.connectionProvider(cp);
    }
}
