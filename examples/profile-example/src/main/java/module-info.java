module colesico.framework.example.profile {

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.profile;
    requires org.apache.commons.lang3;

    exports colesico.framework.example.profile;
    exports colesico.framework.example.profile.custom;
    exports colesico.framework.example.profile.listener;

}