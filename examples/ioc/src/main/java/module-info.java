module colesico.examples.ioc {
    requires transitive colesico.framework.ioc;

    exports colesico.examples.ioc.helloworld;
    exports colesico.examples.ioc.singleton;
    exports colesico.examples.ioc.implement;
    exports colesico.examples.ioc.named;
    exports colesico.examples.ioc.plugin;
    exports colesico.examples.ioc.multiplugin;
    exports colesico.examples.ioc.logger;
}