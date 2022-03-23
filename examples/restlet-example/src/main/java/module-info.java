module colesico.framework.example.restlet {

    requires colesico.framework.bundle.web;
    requires colesico.framework.undertow;
    requires java.net.http;
    requires colesico.framework.openapi;

    exports colesico.framework.example.restlet;
    opens colesico.framework.example.restlet to gson;
}