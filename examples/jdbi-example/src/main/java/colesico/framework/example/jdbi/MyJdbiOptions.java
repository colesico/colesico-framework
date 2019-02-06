package colesico.framework.example.jdbi;

import colesico.framework.config.Configuration;
import colesico.framework.jdbi.JdbiOptions;
import org.jdbi.v3.core.Jdbi;

@Configuration
public class MyJdbiOptions extends JdbiOptions {

    @Override
    public void apply(Jdbi jdbi) {
        System.out.println("JDBI internal config:"+jdbi.getConfig());
    }
}
