package colesico.framework.openapi;

import colesico.framework.http.HttpMethod;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.teleapi.TeleScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

abstract public class OpenApiScheme implements TeleScheme<OpenAPI> {

    public static final String CREATE_API_METHOD = "api";
    public static final String CREATE_OPERATION_METHOD = "operation";
    public static final String CREATE_PARAM_METHOD = "param";
    public static final String API_VAR = "api";
    public static final String OPERATION_VAR = "operation";
    public static final String PARAM_VAR = "param";

    /**
     * Create api
     */
    protected OpenAPI api(String name) {
        // Create  api object
        OpenAPI api = new OpenAPI();
        api.setPaths(new Paths());
        Tag tag = new Tag();
        tag.setName(name);
        api.addTagsItem(tag);
        return api;
    }

    /**
     * Create operation
     */
    protected Operation operation(OpenAPI api, String operationId, String path, String httpMethod) {

        // Create operation object
        Operation operation = new Operation();
        operation.setOperationId(operationId);
        List<String> tagNames = api.getTags().stream().map(t -> t.getName()).collect(Collectors.toList());
        operation.setTags(tagNames);

        // Add or create PathItem object
        Paths pathMap = api.getPaths();
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
            case HttpMethod.HEAD:
                pathItem.setHead(operation);
                break;
            default:
                throw new RuntimeException("Unsupported http method");

        }
        return operation;
    }

    /**
     * Start build param
     */
    protected InputParam param(Type valueType,
                               String paramName,
                               String originName,
                               Class<? extends RestletTeleReader> readerClass,
                               Type jsonRequestType) {
        //TODO:
        return null;

    }
}
