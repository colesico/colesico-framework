module colesico.framework.example.jdbi {

    requires transitive colesico.framework.hikaricp;
    requires transitive colesico.framework.jdbi;
    requires slf4j.api;

    exports colesico.framework.example.jdbi;
    
}