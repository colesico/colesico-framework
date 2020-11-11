module colesico.framework.pebble {

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires transitive colesico.framework.weblet;

    requires transitive io.pebbletemplates;

    // classes
    exports colesico.framework.pebble;
    exports colesico.framework.pebble.internal to colesico.framework.ioc;
}