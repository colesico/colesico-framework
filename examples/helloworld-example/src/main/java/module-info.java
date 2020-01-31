module colesico.framework.example.helloworld {

    requires colesico.framework.bundle.web;
    requires colesico.framework.undertow;
    requires java.net.http;

    exports colesico.framework.example.helloworld;
}