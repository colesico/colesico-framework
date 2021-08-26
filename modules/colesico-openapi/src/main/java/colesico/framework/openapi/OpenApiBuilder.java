package colesico.framework.openapi;

import colesico.framework.http.HttpMethod;
import colesico.framework.telescheme.TeleSchemeBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

abstract public class OpenApiBuilder extends TeleSchemeBuilder<OpenAPI> {

    public static final String CREATE_OPEN_API_METHOD="createOpenApi";
    public static final String CREATE_OPERATION_METHOD="createOperation";
    public static final String ADD_OPERATION_METHOD="addOperation";


    protected OpenAPI createOpenApi() {
        OpenAPI openApi = new OpenAPI();
        openApi.setPaths(new Paths());

        return openApi;
    }

    protected Operation createOperation(String operationId){
        Operation operation = new Operation();
        operation.setOperationId(operationId);
        return operation;
    }

    protected void addOperation(OpenAPI openApi, String path, String httpMethod, Operation operation) {
        Paths pathMap = openApi.getPaths();
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
