module colesico.framework.example.routes {
    requires colesico.framework.bundle.web;
    requires colesico.framework.undertow;

    exports colesico.framework.example.routing;
    exports colesico.framework.example.routing.pkgrelative;
}