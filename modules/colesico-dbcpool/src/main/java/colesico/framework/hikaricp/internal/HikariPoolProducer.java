package colesico.framework.hikaricp.internal;

import colesico.framework.dbcpool.DBCPool;
import colesico.framework.hikaricp.DefaultHikariPoolConfig;
import colesico.framework.hikaricp.HikariPoolConfig;
import colesico.framework.ioc.*;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
@Produce(HikariPool.class)
@Produce(DefaultHikariPoolConfig.class)
public class HikariPoolProducer {

    /**
     * Pool factory
     *
     * @param impl
     * @return
     */
    @Classed(HikariPoolConfig.class)
    @Unscoped
    public DBCPool dbcPoolFactory(HikariPool impl) {
        return impl;
    }

    /**
     * Default pool internal
     *
     * @param factory
     * @return
     */
    @Singleton
    public DBCPool getDefaultDbcPool(@Classed(HikariPoolConfig.class) Supplier<DBCPool> factory, DefaultHikariPoolConfig config) {
        return factory.get(config);
    }

}
