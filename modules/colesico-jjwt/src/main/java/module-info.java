module colesico.framework.jjwt {

    requires transitive colesico.framework.config;

    requires org.slf4j;
    requires colesico.framework.security;

    // API
    exports colesico.framework.jjwt;

    // Internals
    exports colesico.framework.jjwt.internal to colesico.framework.ioc;
}