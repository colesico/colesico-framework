package colesico.framework.httprouter.internal;

import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.httprouter.*;
import colesico.framework.teleapi.TeleController;

import colesico.framework.teleapi.TeleCommand;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class RouterBuilderImpl implements RouterBuilder {

    private final TaskScope taskScope;
    private final Polysupplier<TargetController> targetControllers;
    private final List<CustomRouteAction> customRouteActions = new ArrayList<>();

    @Inject
    public RouterBuilderImpl(Polysupplier<TargetController> targetControllers,
                             TaskScope taskScope) {

        this.targetControllers = targetControllers;
        this.taskScope = taskScope;
    }

    @Override
    public void addCustomAction(HttpMethod httpMethod,
                                String route,
                                TeleController<Router.Criteria, Router.Invocation, RouterCommandsRegistry> teleController,
                                TeleCommand teleCommand,
                                Class<?> targetClass,
                                String targetMethod,
                                Map<String, String> routeAttributes) {

        customRouteActions.add(new CustomRouteAction(httpMethod,
                route, teleController, teleCommand, targetClass,
                targetMethod, routeAttributes)
        );
    }

    @Override
    public Router build() {
        RouterImpl router = new RouterImpl(taskScope);

        for (var teleController : targetControllers) {
            router.register(teleController);
        }

        for (CustomRouteAction cra : customRouteActions) {
            router.addCustomAction(cra.httpMethod(),
                    cra.route(),
                    cra.teleController(),
                    cra.teleCommand(),
                    cra.targetClass(),
                    cra.targetMethod(),
                    cra.attributes());
        }
        return router;
    }

    private record CustomRouteAction(HttpMethod httpMethod,
                                     String route,
                                     TeleController<Router.Criteria, Router.Invocation, RouterCommandsRegistry> teleController,
                                     TeleCommand teleCommand,
                                     Class<?> targetClass,
                                     String targetMethod,
                                     Map<String, String> attributes) {
    }
}
