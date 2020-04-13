module colesico.framework.rpc {


    // Inherited in client projects
    requires transitive colesico.framework.service;
    requires com.squareup.javapoet;
    requires java.compiler;
    requires org.apache.commons.lang3;
    requires org.slf4j;

    exports colesico.framework.rpc;
    exports colesico.framework.rpc.teleapi;
}