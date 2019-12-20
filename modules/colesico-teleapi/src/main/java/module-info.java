module colesico.framework.teleapi {

   // Inherited in client projects
    requires transitive colesico.framework.ioc;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // Exports

    exports colesico.framework.teleapi;
    exports colesico.framework.teleapi.internal to colesico.framework.ioc;

}