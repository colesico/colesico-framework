module colesico.framework.jdbirec {

    requires transitive java.sql;
    requires transitive org.jdbi.v3.core;

    requires static colesico.framework.ioc;
    requires static com.squareup.javapoet;

    requires org.slf4j;

    requires org.apache.commons.lang3;
    requires java.compiler;

    // API
    exports colesico.framework.jdbirec;
    exports colesico.framework.jdbirec.mediators;

    // Internals
    //exports colesico.framework.dao.internal to colesico.framework.ioc;
}