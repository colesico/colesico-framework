package colesico.framework.telehttp.origin;

import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class RouteOrigin implements Origin<String, String> {

    private final Provider<RouterContext> routerContextProv;

    public RouteOrigin(Provider<RouterContext> routerContextProv) {
        this.routerContextProv = routerContextProv;
    }

    @Override
    public String getValue(String key) {
        return routerContextProv.get().getParameters().get(key);
    }
}
