module colesico.framework.example.web {

    requires transitive colesico.framework.undertow;
    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.restlet;
    requires transitive colesico.framework.webstatic;
    requires transitive colesico.framework.pebble;

    requires transitive java.net.http;

    requires org.slf4j;

    exports colesico.framework.example.web.params;
    exports colesico.framework.example.web.staticres;
    exports colesico.framework.example.web.pebble;
    exports colesico.framework.example.web.localization;

    opens colesico.framework.example.web.staticres.webpub;
    opens colesico.framework.example.web.trimou.tmpl;
    opens colesico.framework.example.web.pebble.tmpl;
    opens colesico.framework.example.web.localization.t9n;

}