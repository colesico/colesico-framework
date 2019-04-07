module colesico.framework.widget {

    requires transitive  colesico.framework.ioc;

    requires slf4j.api;
    //requires org.slf4j;

    requires org.apache.commons.lang3;

    // classes
    exports colesico.framework.widget;
    exports colesico.framework.widget.internal to colesico.framework.ioc;
}