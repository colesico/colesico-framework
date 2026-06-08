module colesico.framework.test.example.helloworld {

    requires colesico.framework.example.helloworld;

    requires org.slf4j;
    requires org.testng;
    requires java.net.http;

    requires io.fusionauth.http;

    exports colesico.framework.test.example.helloworld;
}