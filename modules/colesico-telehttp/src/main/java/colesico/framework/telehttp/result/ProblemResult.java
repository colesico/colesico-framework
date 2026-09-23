package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.teleapi.TeleProblem;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * General purpose problem result implementation
 */
public class ProblemResult<D>
        extends AbstractHttpResult
        implements ProblemHttpResult<D> {

    protected final D details;

    public ProblemResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, D details) {
        super(status, contentType, headers, cookies);
        this.details = details;
    }

    public D problemDetails() {
        return details;
    }

    public static <D, R extends ProblemResult<D>, B extends Builder<D, R, B>> Builder<D, R, B> details(D details) {
        return new Builder<>(details);
    }

    public static <R extends ProblemResult<TeleProblem.ProblemDetails>, B extends Builder<TeleProblem.ProblemDetails, R, B>> Builder<TeleProblem.ProblemDetails, R, B> exception(Exception exception) {
        return new Builder<>(TeleProblem.ProblemDetails.of(exception));
    }

    @Override
    public String toString() {
        return "ProblemResult{" +
                "status=" + status +
                ", details=" + details +
                '}';
    }

    public static class Builder<D, R extends ProblemResult<D>, B extends Builder<D, R, B>>
            extends AbstractHttpResult.Builder<R, B> {

        protected D details;

        public B value(D value) {
            this.details = value;
            return self();
        }

        public Builder(D details) {
            this.details = details;
        }

        @Override
        @SuppressWarnings("unchecked")
        public R build() {
            return (R) new ProblemResult<>(status, contentType, headers, cookies, details);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }
    }
}
