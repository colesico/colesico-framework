package colesico.framework.router.internal;

import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.RouterInvocation;
import colesico.framework.router.Router;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterDescriptors;
import colesico.framework.teleapi.TeleController;
import colesico.framework.teleapi.TeleFacade;

import colesico.framework.teleapi.TeleMethod;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class RouterBuilderImpl implements RouterBuilder {

    private final ThreadScope threadScope;
    private final Map<, Polysupplier<TeleFacade>> teleFacades;
    private final List<CustomRouteAction> customRouteActions = new ArrayList<>();

    @Inject
    public RouterBuilderImpl(@Classed(Router.class) Polysupplier<TeleFacade> teleFacades,
                             ThreadScope threadScope) {
        this.teleFacades = teleFacades;
        this.threadScope = threadScope;
    }

    @Override
    public void addCustomAction(HttpMethod httpMethod,
                                String route,
                                TeleMethod<?, ?> teleMethod,
                                Class<?> targetClass,
                                String targetMethod,
                                Map<String, String> routeAttributes) {

        customRouteActions.add(new CustomRouteAction(httpMethod, route, teleMethod, targetClass,
                targetMethod, routeAttributes));
    }

    @Override
    public Router build() {
        RouterImpl router = new RouterImpl(threadScope);
        router.register((TeleFacade<?, RouterDescriptors>) teleFacades);
        for (CustomRouteAction cra : customRouteActions) {
            router.addCustomAction(cra.httpMethod(),
                    cra.route(),
                    cra.teleController(),
                    cra.teleMethod(),
                    cra.targetClass(),
                    cra.targetMethod(),
                    cra.attributes());
        }
        return router;
    }

    private record CustomRouteAction(HttpMethod httpMethod, String route,
                                     TeleController<?, RouterInvocation, ?> teleController, TeleMethod<?, ?> teleMethod,
                                     Class<?> targetClass, String targetMethod, Map<String, String> attributes) {
    }
}
