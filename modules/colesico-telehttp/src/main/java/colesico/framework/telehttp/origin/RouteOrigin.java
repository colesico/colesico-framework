package colesico.framework.telehttp.origin;

import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class RouteOrigin implements Origin {

    private final Provider<RouterContext> routerContextProv;

    public RouteOrigin(Provider<RouterContext> routerContextProv) {
        this.routerContextProv = routerContextProv;
    }

    @Override
    public Collection<String> getStrings(String name) {
        List<String> result = new ArrayList<>();
        var value = routerContextProv.get().parameters().get(name);
        if (value != null) {
            result.add(value);
        }
        return result;
    }
}
