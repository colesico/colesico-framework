module colesico.framework.jdbi {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.transaction;
    requires transitive java.sql;
    requires transitive jdbi3.core;

    requires slf4j.api;
    requires org.apache.commons.lang3;

    // API
    exports colesico.framework.jdbi;

    // Internals
    exports colesico.framework.jdbi.internal to colesico.framework.ioc;
}