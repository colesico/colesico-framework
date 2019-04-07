module colesico.framework.pebble {


    requires slf4j.api;
    //requires org.slf4j;

    requires org.apache.commons.lang3;

    requires transitive colesico.framework.htmlrenderer;

    requires transitive io.pebbletemplates;

    // classes
    exports colesico.framework.pebble;
    exports colesico.framework.pebble.internal to colesico.framework.ioc;
}