package colesico.framework.router.internal;

import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.Router;
import colesico.framework.router.RouterBuilder;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

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
    public void addCustomAction(HttpMethod httpMethod, String route, Class<?> targetClass, TeleMethod targetMethodRef, String targetMethodName) {
        customRouteActions.add(new CustomRouteAction(httpMethod, route, targetClass, targetMethodRef, targetMethodName));
    }

    @Override
    public Router build() {
        RouterImpl router = new RouterImpl(threadScope);
        router.loadRoutesMapping(teleFacadesSupp);
        for (CustomRouteAction cra : customRouteActions) {
            router.addCustomAction(cra.getHttpMethod(), cra.getRoute(), cra.getTargetClass(), cra.getTargetMethodRef(), cra.getTargetMethodName());
        }
        return router;
    }

    private static final class CustomRouteAction {
        private final HttpMethod httpMethod;
        private final String route;
        private final Class<?> targetClass;
        private final TeleMethod targetMethodRef;
        private final String targetMethodName;

        public CustomRouteAction(HttpMethod httpMethod, String route, Class<?> targetClass, TeleMethod targetMethodRef, String targetMethodName) {
            this.httpMethod = httpMethod;
            this.route = route;
            this.targetClass = targetClass;
            this.targetMethodRef = targetMethodRef;
            this.targetMethodName = targetMethodName;
        }

        public HttpMethod getHttpMethod() {
            return httpMethod;
        }

        public String getRoute() {
            return route;
        }

        public Class<?> getTargetClass() {
            return targetClass;
        }

        public TeleMethod getTargetMethodRef() {
            return targetMethodRef;
        }

        public String getTargetMethodName() {
            return targetMethodName;
        }
    }
}
