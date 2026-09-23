package colesico.framework.telehttp.writer;

import colesico.framework.teleapi.TeleProblem;
import colesico.framework.telehttp.*;
import colesico.framework.telehttp.result.ProblemHttpResult;
import colesico.framework.telehttp.result.ProblemResult;
import jakarta.inject.Singleton;

/**
 * Default exception writer
 */
@Singleton
public class ExceptionWriter
        implements HttpWriter<Exception, HttpWriteOptions> {

    private final ProblemResultWriter<ProblemHttpResult<?>, HttpWriteOptions> writer;

    public ExceptionWriter(ProblemResultWriter writer) {
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
    public void write(Exception exception, HttpWriteOptions options) {
        if (exception instanceof ProblemHttpResult<?> phr) {
            writer.write(phr, options);
        } else {
            writer.write(resultBuilder(exception).build(), options);
        }
    }
}

