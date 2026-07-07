module colesico.framework.pebble {

    requires transitive colesico.framework.weblet;
    requires transitive io.pebbletemplates;

    requires org.slf4j;

    exports colesico.framework.pebble;
    exports colesico.framework.pebble.internal to colesico.framework.ioc;
}