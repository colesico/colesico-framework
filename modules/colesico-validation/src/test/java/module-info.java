module colesico.framework.validation.test {

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.translation;

    requires colesico.framework.validation;

    requires org.slf4j;
    requires org.testng;

    exports colesico.framework.test.dslvalidator;

}