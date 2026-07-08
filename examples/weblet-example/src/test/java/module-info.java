module colesico.framework.test.example.weblet {

    requires colesico.framework.example.weblet;

    requires org.slf4j;
    requires org.testng;
    requires java.net.http;

    requires io.fusionauth.http;

    exports colesico.framework.test.example.weblet;
}