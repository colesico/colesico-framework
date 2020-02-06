module colesico.framework.example.web {

    requires colesico.framework.undertow;
    requires colesico.framework.weblet;
    requires colesico.framework.restlet;
    requires colesico.framework.webstatic;
    requires colesico.framework.pebble;

    requires logback.classic;

    exports colesico.framework.example.web.params;
    exports colesico.framework.example.web.staticres;
    exports colesico.framework.example.web.pebble;
    exports colesico.framework.example.web.localization;

    opens colesico.framework.example.web.staticres.webpub;
    opens colesico.framework.example.web.trimou.tmpl;
    opens colesico.framework.example.web.pebble.tmpl;
    opens colesico.framework.example.web.localization.t9n;

}