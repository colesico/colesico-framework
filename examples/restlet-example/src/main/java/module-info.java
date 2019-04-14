module colesico.framework.example.restlet {

    requires colesico.framework.appbundle;
    requires colesico.framework.undertow;

    exports colesico.framework.example.restlet;
    opens colesico.framework.example.restlet to gson;
}