package colesico.framework.router;

import colesico.framework.http.HttpMethod;
import colesico.framework.teleapi.TeleMethod;

import java.util.Map;

public interface RouterBuilder {

    /**
     * Adds a custom action for the specified route.
     * This method is not thread save and should be called at application start time.
     *
     * @param httpMethod      HTTP methods for route   (e.g. GET, POST ,etc)
     * @param route           route definition  (ex: /my/foo )
     * @param teleMethod      action class method
     * @param targetClass     route action class
     * @param targetMethod    action method name
     * @param routeAttributes route attributes {@link RouteAttribute}
     */
    void addCustomAction(HttpMethod httpMethod,
                         String route,
                         TeleMethod teleMethod,
                         Class<?> targetClass,
                         String targetMethod,
                         Map<String, String> routeAttributes);

    Router build();
}
