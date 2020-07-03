module colesico.framework.asynctask {

    requires transitive colesico.framework.ioc;
    requires transitive colesico.framework.config;
    requires transitive colesico.framework.eventbus;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // classes
    exports colesico.framework.asynctask;
    exports colesico.framework.asynctask.internal to colesico.framework.config, colesico.framework.ioc;

}
