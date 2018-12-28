package colesico.framework.fluentjdbc.internal;

import colesico.framework.dba.ConnectionSource;
import colesico.framework.fluentjdbc.FjdbcConfig;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;

import javax.inject.Singleton;

@Singleton
public class FjdbcConfigImpl extends FjdbcConfig {

    private final ConnectionSource connProv;

    public FjdbcConfigImpl(ConnectionSource connProv) {
        this.connProv = connProv;
    }

    @Override
    public void applyOptions(FluentJdbcBuilder builder) {
        org.codejargon.fluentjdbc.api.integration.ConnectionProvider connProv = new SimpleConnectionProvider(this.connProv);
        builder.connectionProvider(connProv);
    }

}
