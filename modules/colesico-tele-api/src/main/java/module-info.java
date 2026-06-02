module colesico.framework.teleapi {

   // Inherited in client projects
    requires transitive colesico.framework.ioc;

    requires org.slf4j;

    // Exports
    exports colesico.framework.teleapi;
    exports colesico.framework.teleapi.assist;
    exports colesico.framework.teleapi.dataport;
    exports colesico.framework.teleapi.internal to colesico.framework.ioc;

}