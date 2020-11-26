module colesico.framework.rpc {

    // Inherited in client projects
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.security;
    requires transitive colesico.framework.profile;

    requires colesico.framework.http;
    requires colesico.framework.router;

    requires org.slf4j;
    requires java.net.http;
    requires static com.squareup.javapoet;
    requires static java.compiler;
    requires org.apache.commons.lang3;
    requires com.esotericsoftware.kryo.kryo5;

    exports colesico.framework.rpc;
    exports colesico.framework.rpc.http;
    exports colesico.framework.rpc.teleapi;
}