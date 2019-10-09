package colesico.framework.jdbi;

import colesico.framework.ioc.Polysupplier;

import javax.sql.DataSource;

public class DefaultJdbiConfig extends JdbiConfigPrototype {

    protected final DataSource dataSource;
    protected final Polysupplier<JdbiOptionsPrototype> options;

    public DefaultJdbiConfig(DataSource dataSource, Polysupplier<JdbiOptionsPrototype> options) {
        this.dataSource = dataSource;
        this.options = options;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Polysupplier<JdbiOptionsPrototype> getOptions() {
        return options;
    }

}
