package colesico.framework.router;

import colesico.framework.http.HttpMethod;
import colesico.framework.teleapi.TeleMethod;

import java.util.Map;

public interface RouterBuilder {

    /**
     * Adds a custom action for the specified route.
     * This method is not thread save and should be called at application start time.
     *
     * @param httpMethod       HTTP methods for route   (e.g. GET, POST ,etc)
     * @param route            reoute definition  (ex: /my/foo )
     * @param targetClass      route action class
     * @param targetMethodRef  action class method reference
     * @param targetMethodName action method name
     * @param routeAttributes       route attributes {@link RouteAttribute}
     */
    void addCustomAction(HttpMethod httpMethod,
                         String route,
                         Class<?> targetClass,
                         TeleMethod targetMethodRef,
                         String targetMethodName,
                         Map<String, String> routeAttributes);

    Router build();
}
