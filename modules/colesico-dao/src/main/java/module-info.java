module colesico.framework.dao {

    requires transitive colesico.framework.ioc;
    requires transitive javax.inject;
    requires transitive java.sql;

    requires static com.squareup.javapoet;

    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires java.compiler;

    // API
    exports colesico.framework.dao;

    // Internals
    //exports colesico.framework.dao.internal to colesico.framework.ioc;
}