module colesico.framework.validation {

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.translation;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // Api
    exports colesico.framework.validation;
    opens colesico.framework.validation;

    exports colesico.framework.dslvalidator;
    exports colesico.framework.dslvalidator.commands;
    exports colesico.framework.dslvalidator.builder;

    exports colesico.framework.dslvalidator.t9n;

    opens colesico.framework.dslvalidator.t9n;
}