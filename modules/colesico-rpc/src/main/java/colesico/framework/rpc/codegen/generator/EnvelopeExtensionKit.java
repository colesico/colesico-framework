package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.ServiceLocator;
import colesico.framework.rpc.codegen.model.RpcApiMethodElement;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EnvelopeExtensionKit {

    protected final Logger logger = LoggerFactory.getLogger(EnvelopeExtensionKit.class);

    protected final List<EnvelopeExtension> extensions = new ArrayList<>();

    public EnvelopeExtensionKit() {
        lookup();
    }

    public void lookup() {
        extensions.clear();
        ServiceLocator<EnvelopeExtension> locator = ServiceLocator.of(this.getClass(), EnvelopeExtension.class, getClass().getClassLoader());
        for (EnvelopeExtension plg : locator.getProviders()) {
            extensions.add(plg);
            logger.debug("Found " + EnvelopeExtension.class.getSimpleName() + ": " + plg.getClass().getName());
        }
    }

    public List<Class<?>> getRequestExtensions() {
        List<Class<?>> result = new ArrayList<>();
        for (EnvelopeExtension plugin : extensions) {
            result.add(plugin.getRequestExtension());
        }
        return result;
    }

    public List<Class<?>> getResponseExtensions() {
        List<Class<?>> result = new ArrayList<>();
        for (EnvelopeExtension plugin : extensions) {
            result.add(plugin.getResponseExtension());
        }
        return result;
    }
}
