module colesico.framework.jjwt {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.security;
    requires transitive colesico.framework.http;
    requires transitive jjwt.api;

    requires org.slf4j;
    requires com.google.gson;

    // API
    exports colesico.framework.jjwt;

    // Internals
    exports colesico.framework.jjwt.internal to colesico.framework.ioc;
}