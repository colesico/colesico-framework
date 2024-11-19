module colesico.framework.validation.test {

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.translation;

    requires static java.compiler;
    requires static com.palantir.javapoet;

    requires org.slf4j;

    requires org.apache.commons.lang3;
    requires colesico.framework.validation;

    exports colesico.framework.test.dslvalidator;
}