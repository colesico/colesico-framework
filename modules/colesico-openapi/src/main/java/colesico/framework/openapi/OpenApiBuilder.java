package colesico.framework.openapi;

import colesico.framework.http.HttpMethod;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.telescheme.TeleSchemeBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

abstract public class OpenApiBuilder implements TeleSchemeBuilder<OpenAPI> {

    public static final String CREATE_OPEN_API_METHOD = "createOpenApi";
    public static final String CREATE_OPERATION_METHOD = "createOperation";
    public static final String CREATE_INPUT_PARAM_METHOD = "createInputParam";

    protected OpenAPI createOpenApi(String name) {
        OpenAPI openApi = new OpenAPI();
        openApi.setPaths(new Paths());
        Tag tag = new Tag();
        tag.setName(name);
        openApi.addTagsItem(tag);

        return openApi;
    }

    protected Operation createOperation(OpenAPI openApi, String operationId, String path, String httpMethod) {

        // Create operation object

        Operation operation = new Operation();
        operation.setOperationId(operationId);
        List<String> tagNames = openApi.getTags().stream().map(t -> t.getName()).collect(Collectors.toList());
        operation.setTags(tagNames);

        // Add or create PathItem object
        Paths pathMap = openApi.getPaths();
        PathItem pathItem = pathMap.get(path);
        if (pathItem == null) {
            pathItem = new PathItem();
            pathMap.addPathItem(path, pathItem);
        }

        // Link operation according http method
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
        return operation;
    }

    protected InputParam createInputParam(OpenAPI openAPI,
                                          Operation operation,
                                          Type valueType,
                                          String paramName,
                                          String originName,
                                          Class<? extends RestletTeleReader> readerClass,
                                          Type jsonRequestType) {
        //TODO:
        return null;

    }
}
