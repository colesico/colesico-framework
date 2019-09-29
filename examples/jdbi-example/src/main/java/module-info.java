module colesico.framework.example.jdbi {

    requires transitive colesico.framework.hikaricp;
    requires transitive colesico.framework.jdbi;

    requires org.slf4j;

    exports colesico.framework.example.jdbi;

}