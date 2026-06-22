package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.production.Supplier;
import colesico.framework.weblet.WebletException;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.ByteBuffer;

@Singleton
public class WebletExceptionWriter implements WebletTeleWriter<WebletException> {

    private final Provider<HttpResponse> httpResponse;

    public WebletExceptionWriter(Provider<HttpResponse> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void write(WebletException value, Class<WebletException> valueType, WebletWriteOptions options) {
        var response = httpResponse.get();

        if (value == null) {
            response.setStatus(500)
                    .setContentType(WebletWriteOptions.DEFAULT_CONTENT_TYPE)
                    .sendText("Unexpected error");
            return;
        }

        var statusCode = value.statusCode();
        if (statusCode == null) {
            statusCode = options.statusCode();
            if (statusCode == null) {
                statusCode = WebletWriteOptions.DEFAULT_ERROR_STATUS_CODE;
            }
        }
        response.setStatus(statusCode);

        var contentType = options.contentType();
        if (contentType == null) {
            contentType = WebletWriteOptions.DEFAULT_CONTENT_TYPE;
        }
        response.setContentType(contentType);

        var charset = options.charset();
        if (charset == null) {
            charset = WebletWriteOptions.DEFAULT_CHARSET;
        }

        String content = String.valueOf(value.details());
        response.sendData(ByteBuffer.wrap(content.getBytes(charset)));
    }

}
