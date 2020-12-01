package colesico.framework.rpc.codegen.generator;

import colesico.framework.rpc.teleapi.BasicEnvelope;

/**
 * Basic envelop extension
 * @see BasicEnvelope
 */
public class BasicExtension implements EnvelopeExtension {
    @Override
    public Class<?> getRequestExtension() {
        return BasicEnvelope.class;
    }

    @Override
    public Class<?> getResponseExtension() {
        return BasicEnvelope.class;
    }
}
