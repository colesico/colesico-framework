package colesico.framework.rpc.teleapi.writer;

import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.teleapi.RpcTWContext;
import colesico.framework.rpc.teleapi.RpcTeleWriter;
import colesico.framework.service.ApplicationException;

import javax.inject.Singleton;

@Singleton
public class ApplicationExceptionWriter implements RpcTeleWriter<ApplicationException> {

    @Override
    public void write(ApplicationException value, RpcTWContext context) {
        context.getResponse().setError(new ApplicationError(value.getMessage()));
    }

    public static class ApplicationError extends RpcError {

        public ApplicationError() {
        }

        public ApplicationError(String message) {
            super(ApplicationException.class.getCanonicalName(), message);
        }
    }
}
