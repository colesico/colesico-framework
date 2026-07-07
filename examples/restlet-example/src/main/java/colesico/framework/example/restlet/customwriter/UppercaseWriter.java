package colesico.framework.example.restlet.customwriter;

import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.restlet.RestletWriter;
import colesico.framework.telehttp.response.StringResponse;
import colesico.framework.telehttp.writer.StringResponseWriter;
import jakarta.inject.Singleton;

@Singleton
public class UppercaseWriter implements RestletWriter<String> {

    private final StringResponseWriter writer;

    public UppercaseWriter(StringResponseWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(String value, RestletWriteOptions options) {
        writer.write(StringResponse.value(value.toUpperCase()).build(), options);
    }
}
