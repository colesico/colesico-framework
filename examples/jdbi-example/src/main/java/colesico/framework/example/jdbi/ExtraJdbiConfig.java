package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.ioc.production.Classed;
import colesico.framework.jdbi.JdbiConfigPrototype;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;

@Config
public class ExtraJdbiConfig extends JdbiConfigPrototype {

    private final DataSource dataSource;

    @Inject
    public ExtraJdbiConfig(@Classed(ExtraHikariProperties.class) DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public void configure(Jdbi jdbi) {
        jdbi.registerArrayType(Short.class, "smallint");
    }
}
