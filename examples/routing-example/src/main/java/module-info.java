module colesico.framework.example.routes {

    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.fusionhttp;
    requires org.slf4j;

    exports colesico.framework.example.routing;
    exports colesico.framework.example.routing.pkgrelative;
}