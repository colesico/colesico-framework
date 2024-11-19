module colesico.framework.example.routes {

    requires transitive colesico.framework.bundle.web;
    requires transitive colesico.framework.undertow;
    requires transitive java.net.http;
    requires transitive undertow.core;

    exports colesico.framework.example.routing;
    exports colesico.framework.example.routing.pkgrelative;
}