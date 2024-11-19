module colesico.framework.rpc {

    // Inherited in client projects
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.security;
    requires transitive colesico.framework.profile;

    requires colesico.framework.http;
    requires colesico.framework.router;

    requires org.slf4j;
    requires java.net.http;
    requires static com.palantir.javapoet;
    requires static java.compiler;
    requires org.apache.commons.lang3;
    requires com.esotericsoftware.kryo.kryo5;

    exports colesico.framework.rpc;
    exports colesico.framework.rpc.teleapi;
    exports colesico.framework.rpc.clientapi;
    exports colesico.framework.rpc.rpcgear.httpbase;
    exports colesico.framework.rpc.rpcgear.kryo;
    exports colesico.framework.rpc.teleapi.reader;
    exports colesico.framework.rpc.teleapi.writer;

    exports colesico.framework.rpc.internal to colesico.framework.ioc, colesico.framework.config;

    opens colesico.framework.rpc.teleapi to com.esotericsoftware.kryo.kryo5;
}