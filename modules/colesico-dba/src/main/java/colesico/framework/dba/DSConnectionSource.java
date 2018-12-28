package colesico.framework.dba;

import javax.sql.DataSource;
import java.sql.Connection;

public class DSConnectionSource implements ConnectionSource {

    protected final DataSource dataSource;

    public DSConnectionSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
