module colesico.framework.test.example.helloworld {

    requires colesico.framework.example.helloworld;
    requires java.net.http;

    requires org.slf4j;
    requires org.testng;

    exports colesico.framework.test.example.helloworld;
}