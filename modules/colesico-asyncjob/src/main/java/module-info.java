module colesico.framework.asyncjob {

    requires transitive colesico.framework.ioc;
    requires transitive colesico.framework.config;
    requires transitive colesico.framework.transaction;
    requires transitive colesico.framework.eventbus;
    requires transitive java.sql;
    requires gson;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // classes
    exports colesico.framework.asyncjob;
    exports colesico.framework.asyncjob.gson;
    exports colesico.framework.asyncjob.assist;
    exports colesico.framework.asyncjob.dao;
    exports colesico.framework.asyncjob.internal to colesico.framework.config, colesico.framework.ioc;

}