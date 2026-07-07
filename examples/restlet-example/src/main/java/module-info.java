module colesico.framework.example.restlet {

    requires transitive colesico.framework.restlet;
    requires transitive colesico.framework.fusionhttp;

    exports colesico.framework.example.restlet;
    exports colesico.framework.example.restlet.customerror;

    opens colesico.framework.example.restlet to com.google.gson;
    exports colesico.framework.example.restlet.helloworld;
    opens colesico.framework.example.restlet.helloworld to com.google.gson;
}