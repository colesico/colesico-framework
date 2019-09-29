import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.restlet {

    requires static java.compiler;
    requires static com.squareup.javapoet;

    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.validation;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires gson;

    // API
    exports colesico.framework.restlet;
    opens colesico.framework.restlet;
    exports colesico.framework.restlet.teleapi;
    exports colesico.framework.restlet.teleapi.gson;

    // Internal
    exports colesico.framework.restlet.internal to colesico.framework.ioc;

    // Codegen
    exports colesico.framework.restlet.codegen;

    // Resources
    opens colesico.framework.restlet.t9n to colesico.framework.localization;

    provides Modulator with colesico.framework.restlet.codegen.RestletModulator;

}