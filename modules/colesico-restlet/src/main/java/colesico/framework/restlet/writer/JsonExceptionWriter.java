package colesico.framework.restlet.writer;

import colesico.framework.restlet.RestletWriter;
import colesico.framework.restlet.RestletWriteOptions;
import colesico.framework.teleapi.assist.TeleProblem;
import colesico.framework.telehttp.result.ProblemHttpResult;
import colesico.framework.telehttp.result.ProblemResult;
import jakarta.inject.Singleton;

@Singleton
public class JsonExceptionWriter implements RestletWriter<Exception> {

    protected final JsonProblemResultWriter writer;

    public JsonExceptionWriter(JsonProblemResultWriter writer) {
        this.writer = writer;
    }

    /**
     * Override this method to provide custom configured builder
     */
    protected ProblemResult.Builder resultBuilder(Exception exception) {
        if (exception instanceof TeleProblem<?> tp) {
            return ProblemResult.details(tp.problemDetails());
        }
        return ProblemResult.exception(exception);
    }

    @Override
    public void write(Exception exception, RestletWriteOptions options) {
        if (exception instanceof ProblemHttpResult<?> phr) {
            writer.write(phr, options);
        } else {
            writer.write(resultBuilder(exception).build(), options);
        }
    }

}
