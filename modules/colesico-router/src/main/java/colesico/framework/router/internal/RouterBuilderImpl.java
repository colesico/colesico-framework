package colesico.framework.router.internal;

import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterBuilder;
import colesico.framework.router.RouterDescriptors;
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
    private final Polysupplier<TeleFacade> teleFacadesSupp;
    private final List<CustomRouteAction> customRouteActions = new ArrayList<>();

    @Inject
    public RouterBuilderImpl(@Classed(Router.class) Polysupplier<TeleFacade> teleFacadesSupp,
                             ThreadScope threadScope) {
        this.teleFacadesSupp = teleFacadesSupp;
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
        router.register((TeleFacade<?, RouterDescriptors>) teleFacadesSupp);
        for (CustomRouteAction cra : customRouteActions) {
            router.addCustomAction(cra.httpMethod(),
                    cra.route(),
                    cra.teleMethod(),
                    cra.targetClass(),
                    cra.targetMethod(),
                    cra.grouteAttributes());
        }
        return router;
    }

    private static final class CustomRouteAction {
        private final HttpMethod httpMethod;
        private final String route;
        private final TeleMethod<?,?> teleMethod;
        private final Class<?> targetClass;
        private final String targetMethod;
        private final Map<String, String> routeAttributes;

        public CustomRouteAction(HttpMethod httpMethod,
                                 String route,
                                 TeleMethod<?,?> teleMethod,
                                 Class<?> targetClass,
                                 String targetMethod,
                                 Map<String, String> routeAttributes) {
            this.httpMethod = httpMethod;
            this.route = route;
            this.targetClass = targetClass;
            this.teleMethod = teleMethod;
            this.targetMethod = targetMethod;
            this.routeAttributes = routeAttributes;
        }

        public HttpMethod httpMethod() {
            return httpMethod;
        }

        public String route() {
            return route;
        }

        public Class<?> targetClass() {
            return targetClass;
        }

        public TeleMethod<?,?> teleMethod() {
            return teleMethod;
        }

        public String targetMethod() {
            return targetMethod;
        }

        public Map<String, String> grouteAttributes() {
            return routeAttributes;
        }
    }
}
