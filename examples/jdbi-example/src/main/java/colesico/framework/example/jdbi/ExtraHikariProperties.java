package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.hikaricp.HikariProperties;

@Config
public class ExtraHikariProperties extends HikariProperties {

    @Override
    protected String getFileName() {
        return "extra-hikari.properties";
    }
}
