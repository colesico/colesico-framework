module colesico.framework.rpc {


    // Inherited in client projects
    requires transitive colesico.framework.service;
    requires colesico.framework.http;

    requires com.squareup.javapoet;
    requires java.compiler;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires com.esotericsoftware.kryo.kryo5;
    requires colesico.framework.security;


    exports colesico.framework.rpc;
    exports colesico.framework.rpc.teleapi;
}