module colesico.framework.example.jdbirec {

    requires transitive colesico.framework.hikaricp;
    requires transitive colesico.framework.jdbi;

    requires transitive colesico.framework.service;
    requires colesico.framework.jdbirec;


    exports colesico.framework.example.jdbirec;

}