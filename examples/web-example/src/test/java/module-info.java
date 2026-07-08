module colesico.framework.test.example.web {

    requires colesico.framework.example.web;
    requires transitive java.net.http;

    requires org.testng;
    requires org.slf4j;

    exports colesico.framework.test.example.web;
}