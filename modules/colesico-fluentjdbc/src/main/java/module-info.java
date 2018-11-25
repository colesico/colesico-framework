module colesico.framework.fluentjdbc {

    requires transitive colesico.framework.dbcpool;

    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires transitive fluentjdbc;

    // API
    exports colesico.framework.fluentjdbc;

    // Internals
    exports colesico.framework.fluentjdbc.internal to colesico.framework.ioc;
}