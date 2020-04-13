module colesico.framework.example.rpc {

    requires colesico.framework.bundle.web;
    requires colesico.framework.undertow;
    requires colesico.framework.rpc;
    requires java.net.http;

    requires colesico.framework.example.rpc.api;

    exports colesico.framework.example.rpc;
}