package colesico.framework.rpc.codegen.generator;

import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import com.squareup.javapoet.TypeSpec;

public interface EnvelopeGeneratorPlugin {
    void onGenerateRequestEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method);
    void onGenerateResponseEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method);
}
