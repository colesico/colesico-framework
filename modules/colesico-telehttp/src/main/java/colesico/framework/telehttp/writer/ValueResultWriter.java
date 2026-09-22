package colesico.framework.telehttp.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.HttpWriteOptions;
import colesico.framework.telehttp.result.ValueResult;
import jakarta.inject.Provider;

abstract public class ValueResultWriter<R extends ValueResult<?>, O extends HttpWriteOptions>
        extends HttpResultWriter<R, O> {

    public ValueResultWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected Integer emptyResult(R result, Integer emptyStatus) {
        if (result.value() == null){
            return emptyStatus;
        }
        return super.emptyResult(result, emptyStatus);
    }
}
