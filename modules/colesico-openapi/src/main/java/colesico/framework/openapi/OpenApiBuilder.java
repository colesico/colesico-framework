package colesico.framework.openapi;

import colesico.framework.http.HttpMethod;
import colesico.framework.telescheme.TeleSchemeBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

abstract public class OpenApiBuilder extends TeleSchemeBuilder<OpenAPI> {

    protected OpenAPI initOpenApi() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setPaths(new Paths());

        return openAPI;
    }

    protected void addOperation(OpenAPI openAPI, String path, String httpMethod, Operation operation) {
        Paths pathMap = openAPI.getPaths();
        PathItem pathItem = pathMap.get(path);
        if (pathItem == null) {
            pathItem = new PathItem();
            pathMap.addPathItem(path, pathItem);
        }

        switch (httpMethod) {
            case HttpMethod.GET:
                pathItem.setGet(operation);
                break;
            case HttpMethod.PUT:
                pathItem.setPut(operation);
                break;
            case HttpMethod.POST:
                pathItem.setPost(operation);
                break;
            case HttpMethod.DELETE:
                pathItem.setDelete(operation);
                break;
            case HttpMethod.OPTIONS:
                pathItem.setOptions(operation);
                break;

        }

    }
}
