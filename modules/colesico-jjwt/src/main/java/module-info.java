module colesico.framework.jjwt {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.security;
    requires transitive colesico.framework.http;
    requires transitive jjwt.api;
    requires transitive io.avaje.jsonb;

    requires org.slf4j;

    // API
    exports colesico.framework.jjwt;

    // Internals
    exports colesico.framework.jjwt.internal to colesico.framework.ioc;

    provides io.jsonwebtoken.io.Serializer with colesico.framework.jjwt.jsonb.JsonbJwtSerializer;
    provides io.jsonwebtoken.io.Deserializer with colesico.framework.jjwt.jsonb.JsonbJwtDeserializer;
}