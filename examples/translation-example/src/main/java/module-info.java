module colesico.framework.example.translation {

    requires colesico.framework.translation;

    exports colesico.framework.example.translation;
    // Open package to able to access .properties files
    opens colesico.framework.example.translation;
}