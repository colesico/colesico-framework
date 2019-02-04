module colesico.examples.jdbi {

    requires transitive colesico.framework.hikaricp;
    requires transitive colesico.framework.jdbi;
    requires slf4j.api;

    exports colesico.examples.jdbi;
    
}