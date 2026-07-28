module colesico.framework.validation {

    requires static java.compiler;
    requires static com.palantir.javapoet;

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.translation;

    requires org.slf4j;

    // Api
    exports colesico.framework.validation;
    opens colesico.framework.validation;

    exports colesico.framework.dslvalidator;
    exports colesico.framework.dslvalidator.command;
    exports colesico.framework.dslvalidator.builder;
    exports colesico.framework.beanvalidation;
    exports colesico.framework.dslvalidator.t9n;

    opens colesico.framework.dslvalidator.t9n;
}