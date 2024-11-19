module colesico.framework.example.translation {

    requires transitive colesico.framework.translation;

    exports colesico.framework.example.translation;

    // Open package to be able to access .properties files
    opens colesico.framework.example.translation;
}