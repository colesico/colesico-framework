module colesico.framework.example.web {

    requires transitive colesico.framework.fusionhttp;
    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.restlet;
    requires transitive colesico.framework.webstatic;
    requires transitive colesico.framework.pebble;

    requires org.slf4j;

    exports colesico.framework.example.web.params;
    exports colesico.framework.example.web.staticres;
    exports colesico.framework.example.web.pebble;
    exports colesico.framework.example.web.localization;

    opens webpub;
    opens tmpl;
    opens t9n;

}