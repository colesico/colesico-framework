package colesico.framework.restlet.teleapi.response;

import colesico.framework.telehttp.response.TeleHttpContentResponse;

public class RestletResponse<T> extends TeleHttpContentResponse<T> {

    public RestletResponse(T content, String contentType, int statusCode) {
        super(content, contentType, statusCode);
    }

    public static <T> RestletResponse<T> of(T value) {
        return new RestletResponse<>(value, null, 200);
    }

    public static <T> RestletResponse<T> of(T value, int statusCode) {
        return new RestletResponse<>(value, null, statusCode);
    }
}
