module colesico.framework.jdbc {

    requires transitive colesico.framework.transaction;
    requires transitive java.sql;

    requires slf4j.api;
    requires org.apache.commons.lang3;

    // API
    exports colesico.framework.jdbc;

    // Internals
    exports colesico.framework.jdbc.internal to colesico.framework.ioc;
}