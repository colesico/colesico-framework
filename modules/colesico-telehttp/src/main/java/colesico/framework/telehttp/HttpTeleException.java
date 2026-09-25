package colesico.framework.telehttp;

import colesico.framework.teleapi.TeleException;
import colesico.framework.teleapi.assist.TeleProblem;
import colesico.framework.telehttp.result.ProblemHttpResult;

public class HttpTeleException
        extends TeleException
        implements ProblemHttpResult<TeleProblem.ProblemDetails> {

    protected Integer status = 500;
    protected ProblemDetails problemDetails;

    public HttpTeleException() {
    }

    public HttpTeleException(ProblemDetails problemDetails, Integer status) {
        this.problemDetails = problemDetails;
        this.status = status;
    }

    public HttpTeleException(String message, Integer status) {
        super(message);
        this.status = status;
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), message);
    }

    public HttpTeleException(Throwable cause, Integer status) {
        super(cause);
        this.status = status;
        this.problemDetails = TeleProblem.ProblemDetails.of(cause.getClass(), cause.getMessage());
    }

    public HttpTeleException(String message) {
        super(message);
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), message);
    }

    public HttpTeleException(String message, Throwable cause) {
        super(message, cause);
        this.problemDetails = TeleProblem.ProblemDetails.of(cause.getClass(), message);
    }

    public HttpTeleException(Throwable cause) {
        super(cause);
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), cause.getMessage());
    }

    @Override
    public ProblemDetails problemDetails() {
        return problemDetails;
    }

    @Override
    public Integer status() {
        return status;
    }
}
