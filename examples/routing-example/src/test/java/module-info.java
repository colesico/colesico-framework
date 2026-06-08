module colesico.framework.test.example.routes {

    requires colesico.framework.example.routes;
    requires org.slf4j;
    requires org.testng;
    requires io.fusionauth.http;
    requires java.net.http;

    exports colesico.framework.test.example.routing;

}