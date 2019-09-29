module colesico.framework.transaction {

    requires transitive colesico.framework.service;

    requires org.slf4j;

    requires org.apache.commons.lang3;
    requires com.squareup.javapoet;
    requires java.compiler;

    // API
    exports colesico.framework.transaction;

}