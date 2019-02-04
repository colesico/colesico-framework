module colesico.examples.web {

    requires colesico.framework.undertow;
    requires colesico.framework.weblet;
    requires colesico.framework.restlet;
    requires colesico.framework.webstatic;
    requires colesico.framework.pebble;

    requires logback.classic;

    exports colesico.examples.web.helloworld;
    exports colesico.examples.web.routes;
    exports colesico.examples.web.params;
    exports colesico.examples.web.staticres;
    exports colesico.examples.web.pebble;
    exports colesico.examples.web.localization;
    exports colesico.examples.web.restlet;

    opens colesico.examples.web.staticres.webpub;
    opens colesico.examples.web.trimou.tmpl;
    opens colesico.examples.web.pebble.tmpl;
    opens colesico.examples.web.localization.t9n;

}