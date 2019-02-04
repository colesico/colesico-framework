module colesico.framework.dao {

    requires transitive colesico.framework.ioc;
    requires transitive java.sql;

    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires java.compiler;
    requires colesico.framework.transaction;

    // API
    exports colesico.framework.dao;

    // Internals
    //exports colesico.framework.dao.internal to colesico.framework.ioc;
}