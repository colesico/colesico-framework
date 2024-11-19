module colesico.framework.example.helloworld {

    requires transitive colesico.framework.bundle.web;
    requires transitive colesico.framework.undertow;
    requires transitive java.net.http;
    requires org.slf4j;

    exports colesico.framework.example.helloworld;
}