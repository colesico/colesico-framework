module colesico.framework.appbundle {

    // basic
    requires transitive colesico.framework.ioc;
    requires transitive colesico.framework.config;
    requires transitive colesico.framework.teleapi;
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.transaction;

    // web
    requires transitive colesico.framework.http;
    requires transitive colesico.framework.router;
    requires transitive colesico.framework.htmlrenderer;
    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.restlet;
    requires transitive colesico.framework.webstatic;

    //db
    requires transitive colesico.framework.hikaricp;
    requires transitive colesico.framework.jdbc;

    // localization
    requires transitive colesico.framework.profile;
    requires transitive colesico.framework.resource;
    requires transitive colesico.framework.translation;

    //mix
    requires transitive colesico.framework.security;
    requires transitive colesico.framework.eventbus;
    requires transitive colesico.framework.validation;

}