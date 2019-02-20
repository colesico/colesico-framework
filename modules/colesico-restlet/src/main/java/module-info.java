import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.restlet {

    requires static java.compiler;
    requires static com.squareup.javapoet;

    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.validation;

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires com.squareup.moshi;
    requires okio;

    // API
    exports colesico.framework.restlet;
    exports colesico.framework.restlet.teleapi;
    exports colesico.framework.restlet.teleapi.converter;

    // Internal
    exports colesico.framework.restlet.internal to colesico.framework.ioc;

    // Codegen
    exports colesico.framework.restlet.codegen;

    // Resources
    opens colesico.framework.restlet.t9n to colesico.framework.localization;

    provides Modulator with colesico.framework.restlet.codegen.RestletModulator;

}