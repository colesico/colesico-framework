package colesico.framework.example.restlet.customwriter;

import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.restlet.RestletWriter;
import colesico.framework.telehttp.result.StringResult;
import colesico.framework.telehttp.writer.StringResultWriter;
import jakarta.inject.Singleton;

@Singleton
public class UppercaseWriter implements RestletWriter<String> {

    private final StringResultWriter writer;

    public UppercaseWriter(StringResultWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(String value, RestletWriteOptions options) {
        writer.write(StringResult.value(value.toUpperCase()).build(), options);
    }
}
