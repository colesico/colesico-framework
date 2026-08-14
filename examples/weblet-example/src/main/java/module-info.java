module colesico.framework.example.weblet {

    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.fusionhttp;
    requires org.slf4j;
    requires jdk.httpserver;

    exports colesico.framework.example.weblet;
}