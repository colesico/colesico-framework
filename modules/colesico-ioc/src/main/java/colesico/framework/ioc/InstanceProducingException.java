package colesico.framework.ioc;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class InstanceProducingException extends RuntimeException {
    private Class<?> target;

    public InstanceProducingException(Throwable cause, Class<?> target) {
        super(cause);
        this.target = target;
    }

    @Override
    public String getMessage() {
        String errMsg = "An exception occurred while producing instance of " + target.getName();
        if (getCause() instanceof StackOverflowError) {
            errMsg = errMsg + "; Possible the cyclic dependency";
        }
        errMsg = errMsg + "; Root message: " + ExceptionUtils.getRootCauseMessage(this);
        return errMsg;
    }
}
