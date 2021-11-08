package colesico.framework.example.jdbi;

import colesico.framework.config.Config;
import colesico.framework.jdbi.JdbiOptionsPrototype;
import org.jdbi.v3.core.Jdbi;

/**
 * Config for make additional configuration for default jdbi instance
 */
@Config
public class JdbiOptions extends JdbiOptionsPrototype {
    @Override
    public void applyOptions(Jdbi jdbi) {
        // Configure JDBI here
        jdbi.registerArrayType(Short.class, "smallint");
    }
}
