package colesico.framework.jdbi;

import colesico.framework.ioc.Polysupplier;

import javax.sql.DataSource;

public class DefaultJdbiConfig extends JdbiConfig {

    protected final DataSource dataSource;
    protected final Polysupplier<JdbiOptions> options;

    public DefaultJdbiConfig(DataSource dataSource, Polysupplier<JdbiOptions> options) {
        this.dataSource = dataSource;
        this.options = options;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Polysupplier<JdbiOptions> getOptions() {
        return options;
    }

}
