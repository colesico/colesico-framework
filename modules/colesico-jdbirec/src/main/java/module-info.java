module colesico.framework.jdbirec {

    requires transitive colesico.framework.ioc;
    requires transitive javax.inject;
    requires transitive java.sql;
    requires transitive jdbi3.core;

    requires static com.squareup.javapoet;

    requires slf4j.api;
    //requires org.slf4j;

    requires org.apache.commons.lang3;
    requires java.compiler;

    // API
    exports colesico.framework.jdbirec;

    // Internals
    //exports colesico.framework.dao.internal to colesico.framework.ioc;
}