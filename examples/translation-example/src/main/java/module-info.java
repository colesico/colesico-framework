module colesico.framework.example.translation {

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.translation;

    exports colesico.framework.example.translation;
    exports colesico.framework.example.translation.formatter;
    exports colesico.framework.example.translation.dictionary;

    // Open package to  be able to  access .properties files
    opens colesico.framework.example.translation.dictionary;
    opens colesico.framework.example.translation.dictionaryext;

}