package colesico.framework.jdbi.internal;

import colesico.framework.ioc.*;
import colesico.framework.jdbi.JdbiConfig;
import org.jdbi.v3.core.Jdbi;

@Producer(Rank.RANK_MINOR)
public class JdbiProducer {

    /**
     * Jdbi factory
     * Creates Jdbi instance configured with settings
     *
     * @param settings
     * @return
     */
    @Unscoped
    @Classed(JdbiConfig.class)
    public Jdbi jdbiFactory(@Message JdbiConfig settings) {
        final Jdbi jdbi = Jdbi.create(settings.getDataSource());
        jdbi.installPlugins();
        settings.getOptions().forEach(o -> o.apply(jdbi), null);
        return jdbi;
    }

}
