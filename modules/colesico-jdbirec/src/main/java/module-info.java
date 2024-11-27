module colesico.framework.jdbirec {

    // Compile time
    requires static colesico.framework.ioc;
    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires transitive java.sql;
    requires transitive org.jdbi.v3.core;

    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires java.desktop;

    // API
    exports colesico.framework.jdbirec;
    exports colesico.framework.jdbirec.mediators;

    // Internals

}