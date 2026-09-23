package colesico.framework.telehttp;

import colesico.framework.teleapi.TeleException;
import colesico.framework.teleapi.TeleProblem;
import colesico.framework.telehttp.result.ProblemHttpResult;

public class HttpTeleException
        extends TeleException
        implements ProblemHttpResult<Object> {

    protected Integer status = 500;
    protected Object problemDetails;

    public HttpTeleException() {
    }

    public HttpTeleException(Object problemDetails, Integer status) {
        this.problemDetails = problemDetails;
        this.status = status;
    }

    public HttpTeleException(String message, Integer status) {
        super(message);
        this.status = status;
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), message);
    }

    public HttpTeleException(String message) {
        super(message);
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), message);
    }

    public HttpTeleException(String message, Throwable cause) {
        super(message, cause);
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), message);
    }

    public HttpTeleException(Throwable cause) {
        super(cause);
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass());
    }

    public HttpTeleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.problemDetails = TeleProblem.ProblemDetails.of(this.getClass(), message);
    }

    @Override
    public Object problemDetails() {
        return problemDetails;
    }

    @Override
    public Integer status() {
        return status;
    }
}
