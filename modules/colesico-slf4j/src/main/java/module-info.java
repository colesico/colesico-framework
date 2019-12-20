module colesico.framework.slf4j {

    
    // Inherited in client projects
    requires transitive colesico.framework.service;

    requires transitive org.slf4j;

    exports colesico.framework.slf4j;
}