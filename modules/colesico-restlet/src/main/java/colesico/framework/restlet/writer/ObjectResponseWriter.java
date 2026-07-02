package colesico.framework.restlet.writer;

import colesico.framework.teleapi.dataport.WriteOptions;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.TeleHttpWriteOptions;
import colesico.framework.telehttp.response.ObjectResponse;
import colesico.framework.telehttp.response.TeleHttpResponse;
import colesico.framework.telehttp.writer.ValueResponseWriter;

import java.io.IOException;
import java.io.OutputStream;

public class ObjectResponseWriter extends ValueResponseWriter<ObjectResponse> {

    @Override
    protected ContentType defaultContentType() {
        return ContentType.APPLICATION_JSON;
    }

    @Override
    protected void write(OutputStream outputStream, TeleHttpResponse response, TeleHttpWriteOptions options) throws IOException {

    }

    @Override
    public void write(Object value, WriteOptions options) {

    }
}
