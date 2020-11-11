
module colesico.framework.telehttp {

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires transitive colesico.framework.router;
    requires transitive colesico.framework.translation;
    requires transitive colesico.framework.security;

    exports colesico.framework.telehttp;
    exports colesico.framework.telehttp.reader;
    exports colesico.framework.telehttp.writer;
    exports colesico.framework.telehttp.assist;
    exports colesico.framework.telehttp.codegen;
    exports colesico.framework.telehttp.internal to colesico.framework.ioc;
    //exports colesico.framework.telehttp.config to colesico.framework.ioc, colesico.framework.config;

    exports colesico.framework.telehttp.t9n;
    opens colesico.framework.telehttp.t9n;
}
