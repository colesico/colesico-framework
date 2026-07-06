module colesico.framework.test.example.restlet {

    requires colesico.framework.example.restlet;

    requires org.slf4j;
    requires org.testng;
    requires java.net.http;

    requires io.fusionauth.http;

    exports colesico.framework.test.example.restlet;
}