module colesico.framework.example.rpc.api {

    requires transitive colesico.framework.rpc;

    exports colesico.framework.example.rpc.api;
    opens colesico.framework.example.rpc.api;
}