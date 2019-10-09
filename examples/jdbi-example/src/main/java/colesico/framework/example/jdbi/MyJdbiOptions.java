package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.jdbi.JdbiOptionsPrototype;
import org.jdbi.v3.core.Jdbi;

@Config
public class MyJdbiOptions extends JdbiOptionsPrototype {

    @Override
    public void apply(Jdbi jdbi) {
        System.out.println("JDBI internal config:"+jdbi.getConfig());
    }
}
