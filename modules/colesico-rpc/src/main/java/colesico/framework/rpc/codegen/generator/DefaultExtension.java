package colesico.framework.rpc.codegen.generator;

import colesico.framework.rpc.teleapi.RpcEnvelope;

/**
 * Default envelop extension
 */
public class DefaultExtension implements EnvelopeExtension {
    @Override
    public Class<?> getRequestExtension() {
        return RpcEnvelope.class;
    }

    @Override
    public Class<?> getResponseExtension() {
        return null;
    }
}
