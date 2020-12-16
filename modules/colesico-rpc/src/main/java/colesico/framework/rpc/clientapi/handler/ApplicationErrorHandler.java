package colesico.framework.rpc.clientapi.handler;

import colesico.framework.rpc.clientapi.RpcErrorHandler;
import colesico.framework.rpc.teleapi.writer.ApplicationExceptionWriter;
import colesico.framework.service.ApplicationException;

public class ApplicationErrorHandler implements RpcErrorHandler<ApplicationExceptionWriter.ApplicationError> {

    @Override
    public RuntimeException createException(ApplicationExceptionWriter.ApplicationError error) {
        return new ApplicationException(error.getMessage());
    }
}
