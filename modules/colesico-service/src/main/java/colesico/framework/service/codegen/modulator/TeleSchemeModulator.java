package colesico.framework.service.codegen.modulator;

import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.generator.TeleSchemeGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleSchemeElement;
import colesico.framework.teleapi.TeleScheme;

abstract public class TeleSchemeModulator extends Modulator {

    /**
     * Check the tele-scheme can be created for given tele-facade
     */
    abstract protected boolean isTeleServiceSupported(TeleServiceElement teleService);

    /**
     * Scheme implementation type
     *
     * @see TeleScheme
     */
    abstract protected Class<?> schemeType();

    /**
     * Called to process tele facade after parsing completed.
     * Override this method to generate tele-scheme build method body.
     */
    protected abstract void processTeleService(TeleServiceElement teleService);

    /**
     * Creates tele-scheme element.
     * This is default implementation and can be overridden
     * for concrete tele-scheme element
     */
    protected TeleSchemeElement createTeleScheme(TeleServiceElement teleService) {
        TeleSchemeElement schemeBuilder = new TeleSchemeElement(teleService, schemeType(), teleSchemeBaseClass());
        return schemeBuilder;
    }

    /**
     * Returns not null value to override scheme builder base class that be extended by generated tele scheme builder.
     * Default base class - {@link TeleScheme <?>}
     */
    protected Class<? extends TeleScheme> teleSchemeBaseClass() {
        return null;
    }

    /**
     * Helper for scheme builder element obtaining from tele-facade
     */
    protected TeleSchemeElement teleScheme() {
        if (service.teleService() == null) {
            return null;
        }
        return service.teleService().teleScheme(schemeType());
    }

    @Override
    public void onBeforeParseTeleService(TeleServiceElement teleService) {
        super.onBeforeParseTeleService(teleService);
        if (!isTeleServiceSupported(teleService)) {
            return;
        }
        TeleSchemeElement teleScheme = createTeleScheme(teleService);
        teleService.setTeleScheme(schemeType(), teleScheme);
    }

    @Override
    public void onTeleServiceParsed(TeleServiceElement teleService) {
        super.onTeleServiceParsed(teleService);
        if (!isTeleServiceSupported(teleService)) {
            return;
        }
        processTeleService(teleService);
    }

    @Override
    public void onServiceGenerated(ServiceElement service) {
        super.onServiceGenerated(service);
        TeleSchemeElement schemeBuilder = teleScheme();
        if (schemeBuilder == null) {
            return;
        }
        TeleSchemeGenerator teleSchemeGenerator = new TeleSchemeGenerator(processorContext().processingEnv());
        teleSchemeGenerator.generate(schemeBuilder);
    }

    @Override
    public void onGenerateIocProducer(ProducerGenerator generator, ServiceElement service) {
        super.onGenerateIocProducer(generator, service);
        TeleSchemeElement<?> schemeBuilder = teleScheme();
        if (schemeBuilder == null) {
            return;
        }
        // TODO: generate Ioc producer
    }
}
