module colesico.framework.jdbi {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.transaction;
    requires transitive colesico.framework.hikaricp;
    requires transitive java.sql;
    requires transitive org.jdbi.v3.core;



    requires org.slf4j;

    requires org.apache.commons.lang3;

    // API
    exports colesico.framework.jdbi;

    // Internals
    exports colesico.framework.jdbi.internal to colesico.framework.ioc;
}