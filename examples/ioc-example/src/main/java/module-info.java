module colesico.framework.example.ioc {

    requires transitive colesico.framework.ioc;

    exports colesico.framework.example.ioc;
    exports colesico.framework.example.ioc.helloworld;
    exports colesico.framework.example.ioc.singleton;
    exports colesico.framework.example.ioc.implement;
    exports colesico.framework.example.ioc.named;
    exports colesico.framework.example.ioc.substitute;
    exports colesico.framework.example.ioc.multiplugin;
    exports colesico.framework.example.ioc.message;
    exports colesico.framework.example.ioc.logger;
}