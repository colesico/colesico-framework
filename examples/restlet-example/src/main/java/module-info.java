module colesico.framework.example.restlet {

    requires transitive colesico.framework.bundle.web;
    requires transitive colesico.framework.undertow;
    requires transitive java.net.http;
    requires transitive  undertow.core;

    exports colesico.framework.example.restlet;
    exports colesico.framework.example.restlet.customexception;

    opens colesico.framework.example.restlet to com.google.gson;
}