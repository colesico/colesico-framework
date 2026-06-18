package colesico.framework.restlet.teleapi.response;

import colesico.framework.telehttp.response.TeleHttpContentResponse;

/**
 * General purpose restlet response
 */
public class RestletResponse<T> extends TeleHttpContentResponse<T> {

    public RestletResponse(T content, String contentType, int statusCode) {
        super(content, contentType, statusCode);
    }

    public static <T> RestletResponse<T> of(T content) {
        return new RestletResponse<>(content, null, 200);
    }

    public static <T> RestletResponse<T> of(T content, int statusCode) {
        return new RestletResponse<>(content, null, statusCode);
    }
}
