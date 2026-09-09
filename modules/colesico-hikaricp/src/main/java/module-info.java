module colesico.framework.hikaricp {

    requires transitive colesico.framework.config;
    requires transitive java.sql;
    requires transitive com.zaxxer.hikari;

    requires org.slf4j;

    // API
    exports colesico.framework.hikaricp;
    exports colesico.framework.hikaricp.internal to colesico.framework.ioc, colesico.framework.config;

    // Internals
}