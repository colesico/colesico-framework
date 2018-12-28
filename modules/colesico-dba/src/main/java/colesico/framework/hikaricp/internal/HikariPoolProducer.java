package colesico.framework.hikaricp.internal;

import colesico.framework.dba.ConnectionSource;
import colesico.framework.hikaricp.DefaultHikariPoolConfig;
import colesico.framework.hikaricp.HikariPoolConfig;
import colesico.framework.ioc.*;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
@Produce(HikariConnectionSource.class)
@Produce(DefaultHikariPoolConfig.class)
public class HikariPoolProducer {

    /**
     * Connections provider factory
     *
     * @param impl
     * @return
     */
    @Classed(HikariPoolConfig.class)
    @Unscoped
    public ConnectionSource dbRefFactory(HikariConnectionSource impl) {
        return impl;
    }

    /**
     * Default connections provider internal
     *
     * @param factory
     * @return
     */
    @Singleton
    public ConnectionSource getDefaultDBRef(@Classed(HikariPoolConfig.class) Supplier<ConnectionSource> factory, DefaultHikariPoolConfig config) {
        return factory.get(config);
    }

}
