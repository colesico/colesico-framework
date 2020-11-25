package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.ServiceLocator;
import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EnvelopeGeneratorPluginKit {

    protected final Logger logger = LoggerFactory.getLogger(EnvelopeGeneratorPluginKit.class);

    protected final List<EnvelopeGeneratorPlugin> plugins = new ArrayList<>();

    public EnvelopeGeneratorPluginKit() {
        lookup();
    }

    public void lookup() {
        plugins.clear();
        ServiceLocator<EnvelopeGeneratorPlugin> locator = ServiceLocator.of(this.getClass(), EnvelopeGeneratorPlugin.class, getClass().getClassLoader());
        for (EnvelopeGeneratorPlugin plg : locator.getProviders()) {
            plugins.add(plg);
            logger.debug("Found " + EnvelopeGeneratorPlugin.class.getSimpleName() + ": " + plg.getClass().getName());
        }
    }

    public void notifyGenerateRequestEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method) {
        for (EnvelopeGeneratorPlugin plugin : plugins) {
            plugin.onGenerateRequestEnvelope(envelopeBuilder, method);
        }
    }

    public void notifyGenerateResponseEnvelope(TypeSpec.Builder envelopeBuilder, RpcApiMethodElement method) {
        for (EnvelopeGeneratorPlugin plugin : plugins) {
            plugin.onGenerateResponseEnvelope(envelopeBuilder, method);
        }
    }
}
