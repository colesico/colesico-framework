module colesico.framework.example.validation {

    requires transitive colesico.framework.validation;
    requires org.slf4j;

    exports colesico.framework.example.validation;
    exports colesico.framework.example.validation.dto;
}