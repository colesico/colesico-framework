module colesico.framework.example.asynctask {

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.task;

    exports colesico.framework.example.asynctask;
    exports colesico.framework.example.asynctask.performer;
    exports colesico.framework.example.asynctask.eventbus;

}
