module colesico.framework.example.jdbi {

    requires transitive colesico.framework.hikaricp;
    requires transitive colesico.framework.jdbi;

    requires slf4j.api;
    //requires org.slf4j;

    exports colesico.framework.example.jdbi;
    
}