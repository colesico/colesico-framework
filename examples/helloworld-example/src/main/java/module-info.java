module colesico.framework.example.helloworld {

    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.undertow;
    requires transitive java.net.http;
    requires org.slf4j;

    exports colesico.framework.example.helloworld;
}