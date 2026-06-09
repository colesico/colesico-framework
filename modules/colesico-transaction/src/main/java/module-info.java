module colesico.framework.transaction {

    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires transitive colesico.framework.service;
    requires org.slf4j;

    // API
    exports colesico.framework.transaction;

}