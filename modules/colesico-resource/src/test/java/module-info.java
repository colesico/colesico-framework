module colesico.framework.resource.test {

    requires org.slf4j;
    requires static org.testng;

    // Inherited in client projects
    requires transitive colesico.framework.config;
    requires transitive colesico.framework.profile;
    requires colesico.framework.resource;

    // Exports
    exports colesico.framework.test.resource;

}