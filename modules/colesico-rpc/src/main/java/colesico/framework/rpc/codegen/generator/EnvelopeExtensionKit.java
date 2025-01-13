package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.spi.DefaultServiceLocatorFactory;
import colesico.framework.assist.spi.ServiceLocator;
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
        logger.debug("Lookup envelops extensions...");
        extensions.clear();
        ServiceLocator<EnvelopeExtension> extensions = DefaultServiceLocatorFactory.of().locator(this.getClass(), EnvelopeExtension.class, getClass().getClassLoader());
        for (EnvelopeExtension envExt : extensions) {
            this.extensions.add(envExt);
            logger.debug("Found envelope extension : " + envExt.getClass().getName());
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
