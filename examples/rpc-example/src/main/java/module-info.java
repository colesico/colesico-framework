module colesico.framework.example.rpc {

    requires transitive colesico.framework.undertow;
    requires transitive colesico.framework.rpc;
    requires transitive java.net.http;

    requires transitive colesico.framework.example.rpc.api;

    exports colesico.framework.example.rpc;
}