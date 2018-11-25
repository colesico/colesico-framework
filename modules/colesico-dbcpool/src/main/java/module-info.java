module colesico.framework.dbcpool {
    requires transitive colesico.framework.config;
    requires transitive java.sql;

    requires slf4j.api;

    requires org.apache.commons.lang3;
    requires com.zaxxer.hikari;

    // API
    exports colesico.framework.dbcpool;
    exports colesico.framework.hikaricp;

    // Internals
    exports colesico.framework.hikaricp.internal to colesico.framework.ioc;
}