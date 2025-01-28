module colesico.framework.resource {

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // Inherited in client projects
    requires transitive colesico.framework.config;
    requires transitive colesico.framework.profile;


    // Exports
    exports colesico.framework.resource;
    exports colesico.framework.resource.assist;
    exports colesico.framework.resource.assist.localization;
    exports colesico.framework.resource.rewriting;
    exports colesico.framework.resource.internal to colesico.framework.ioc;
    exports colesico.framework.resource.internal.l10n to colesico.framework.ioc;
}