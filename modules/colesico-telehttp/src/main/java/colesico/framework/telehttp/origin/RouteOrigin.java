package colesico.framework.telehttp.origin;

import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class RouteOrigin implements Origin {

    private final Provider<RouterContext> routerContextProv;

    public RouteOrigin(Provider<RouterContext> routerContextProv) {
        this.routerContextProv = routerContextProv;
    }

    @Override
    public String getString(String name) {
        return routerContextProv.get().getParameters().get(name);
    }
}
