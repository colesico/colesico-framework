package colesico.framework.service.codegen.modulator;

import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.generator.TeleSchemeGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.teleapi.TeleScheme;
import colesico.framework.service.codegen.model.TeleSchemeElement;

abstract public class TeleSchemeModulator extends Modulator {

    /**
     * Check the tele-scheme can be created for given tele-facade
     */
    abstract protected boolean isTeleFacadeSupported(TeleFacadeElement teleFacade);

    /**
     * Scheme implementation type
     *
     * @see TeleScheme
     */
    abstract protected Class<?> getSchemeType();

    /**
     * Called to process tele facade after parsing completed.
     * Override this method to generate tele-scheme build method body.
     */
    protected abstract void processTeleFacade(TeleFacadeElement teleFacade);

    /**
     * Creates tele-scheme element.
     * This is default implementation and can be overridden
     * for concrete tele-scheme element
     */
    protected TeleSchemeElement createTeleScheme(TeleFacadeElement teleFacade) {
        TeleSchemeElement schemeBuilder = new TeleSchemeElement(teleFacade, getSchemeType(), getTeleSchemeBaseClass());
        return schemeBuilder;
    }

    /**
     * Returns not null value to override scheme builder base class that be extended by generated tele scheme builder.
     * Default base class - {@link TeleScheme <?>}
     */
    protected Class<? extends TeleScheme> getTeleSchemeBaseClass() {
        return null;
    }

    /**
     * Helper for scheme builder element obtaining from tele-facade
     */
    protected TeleSchemeElement getTeleScheme() {
        if (service.getTeleFacade() == null) {
            return null;
        }
        return service.getTeleFacade().getTeleScheme(getSchemeType());
    }

    @Override
    public void onBeforeParseTeleFacade(TeleFacadeElement teleFacade) {
        super.onBeforeParseTeleFacade(teleFacade);
        if (!isTeleFacadeSupported(teleFacade)) {
            return;
        }
        TeleSchemeElement teleScheme = createTeleScheme(teleFacade);
        teleFacade.setTeleScheme(getSchemeType(), teleScheme);
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!isTeleFacadeSupported(teleFacade)) {
            return;
        }
        processTeleFacade(teleFacade);
    }

    @Override
    public void onServiceGenerated(ServiceElement service) {
        super.onServiceGenerated(service);
        TeleSchemeElement schemeBuilder = getTeleScheme();
        if (schemeBuilder == null) {
            return;
        }
        TeleSchemeGenerator teleSchemeGenerator = new TeleSchemeGenerator(getProcessorContext().getProcessingEnv());
        teleSchemeGenerator.generate(schemeBuilder);
    }

    @Override
    public void onGenerateIocProducer(ProducerGenerator generator, ServiceElement service) {
        super.onGenerateIocProducer(generator, service);
        TeleSchemeElement<?> schemeBuilder = getTeleScheme();
        if (schemeBuilder == null) {
            return;
        }
        // TODO: generate Ioc producer
    }
}
