package colesico.framework.telehttp.origin;

import colesico.framework.httprouter.RouterContext;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class RouteOrigin implements Origin {

    protected final Provider<RouterContext> routerContext;

    public RouteOrigin(Provider<RouterContext> routerContext) {
        this.routerContext = routerContext;
    }

    @Override
    public Collection<String> getStrings(String name) {
        List<String> result = new ArrayList<>();
        var value = routerContext.get().parameters().get(name);
        if (value != null) {
            result.add(value);
        }
        return result;
    }
}
