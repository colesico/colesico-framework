module colesico.framework.rpc {

    // Inherited in client projects
    requires transitive colesico.framework.service;
    requires transitive  colesico.framework.security;
    requires transitive colesico.framework.profile;

    requires colesico.framework.http;
    requires colesico.framework.router;

    requires com.squareup.javapoet;
    requires java.compiler;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires com.esotericsoftware.kryo.kryo5;



    exports colesico.framework.rpc;
    exports colesico.framework.rpc.client;
    exports colesico.framework.rpc.teleapi;
}