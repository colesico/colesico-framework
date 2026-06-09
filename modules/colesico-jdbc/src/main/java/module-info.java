module colesico.framework.jdbc {

    requires transitive colesico.framework.transaction;
    requires transitive java.sql;
    requires org.slf4j;

    // API
    exports colesico.framework.jdbc;
    exports colesico.framework.jdbc.assist;

    // Internals
    exports colesico.framework.jdbc.internal to colesico.framework.ioc;
}