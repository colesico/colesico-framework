import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.weblet {

    requires static java.compiler;
    requires static auto.service;
    requires static com.squareup.javapoet;

    requires transitive colesico.framework.security;
    requires transitive colesico.framework.profile;
    requires transitive colesico.framework.translation;
    requires transitive colesico.framework.router;

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;

    requires kryo;

    exports colesico.framework.weblet;
    exports colesico.framework.weblet.assist;
    exports colesico.framework.weblet.teleapi;
    exports colesico.framework.weblet.teleapi.writer;
    exports colesico.framework.weblet.teleapi.reader;

    exports colesico.framework.weblet.codegen;
    exports colesico.framework.weblet.internal to colesico.framework.ioc;

    opens colesico.framework.weblet.t9n;

    provides Modulator with colesico.framework.weblet.codegen.WebletModulator;
}