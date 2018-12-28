import colesico.framework.service.codegen.modulator.Modulator;

module colesico.framework.eventbus {

    requires transitive colesico.framework.ioc;
    requires transitive colesico.framework.service;

    //requires org.slf4j;
    requires auto.service;
    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires java.compiler;
    requires com.squareup.javapoet;


    // classes
    exports colesico.framework.eventbus;
    exports colesico.framework.eventbus.internal to colesico.framework.ioc;

    provides Modulator with colesico.framework.eventbus.codegen.EventBusModulator;
}