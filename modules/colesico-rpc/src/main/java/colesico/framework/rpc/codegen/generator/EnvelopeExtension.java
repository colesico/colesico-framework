package colesico.framework.rpc.codegen.generator;

public interface EnvelopeExtension {
    Class<?> getRequestExtension();
    Class<?> getResponseExtension();
}
