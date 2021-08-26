package colesico.framework.telescheme.codegen.modulator;

import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.telescheme.TeleSchemeBuilder;
import colesico.framework.telescheme.codegen.generator.TeleSchemeBuilderGenerator;
import colesico.framework.telescheme.codegen.model.TeleSchemeBuilderElement;
import com.squareup.javapoet.CodeBlock;

abstract public class TeleSchemeModulator extends Modulator {

    abstract protected boolean isTeleFacadeSupported(TeleFacadeElement teleFacade);

    /**
     * Tele-facade scheme type
     */
    abstract protected Class<?> getTeleSchemeType();

    /**
     * Tele-method scheme type
     */
    abstract protected Class<?> getOperationSchemeType();

    abstract protected CodeBlock generateScheme(TeleFacadeElement teleFacade);

    /**
     * Called to process tele method after parsing completed.
     * Override this method to custom processing.
     */
    abstract protected CodeBlock generateOperationScheme(TeleMethodElement teleMethod);

    /**
     * Returns not null value to override scheme builder base class that be extended by generated tele scheme builder.
     * Default base class - {@link TeleSchemeBuilder <?>}
     */
    protected Class<? extends TeleSchemeBuilder> getBuilderBaseClass(){
        return null;
    }


    protected TeleSchemeBuilderElement getTeleScheme() {
        if (service.getTeleFacade() == null) {
            return null;
        }
        return service.getTeleFacade().getProperty(TeleSchemeBuilderElement.class);
    }

    @Override
    public void onBeforeParseTeleFacade(TeleFacadeElement teleFacade) {
        super.onBeforeParseTeleFacade(teleFacade);
        if (!isTeleFacadeSupported(teleFacade)) {
            return;
        }
        TeleSchemeBuilderElement facadeScheme = new TeleSchemeBuilderElement(teleFacade, getTeleSchemeType(), getBuilderBaseClass());
        teleFacade.setProperty(TeleSchemeBuilderElement.class, facadeScheme);
    }

    @Override
    public void onTeleMethodParsed(TeleMethodElement teleMethod) {
        super.onTeleMethodParsed(teleMethod);
        if (!isTeleFacadeSupported(teleMethod.getParentTeleFacade())) {
            return;
        }

        CodeBlock ms = generateOperationScheme(teleMethod);
        //TODO
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!isTeleFacadeSupported(teleFacade)) {
            return;
        }

        CodeBlock fs = generateScheme(teleFacade);
        getTeleScheme().setBuildCode(fs);
    }

    @Override
    public void onServiceGenerated(ServiceElement service) {
        super.onServiceGenerated(service);
        TeleSchemeBuilderElement teleScheme = getTeleScheme();
        if (teleScheme == null) {
            return;
        }
        TeleSchemeBuilderGenerator teleSchemeBuilderGenerator = new TeleSchemeBuilderGenerator(getProcessorContext().getProcessingEnv());
        teleSchemeBuilderGenerator.generate(teleScheme);
    }

    @Override
    public void onGenerateIocProducer(ProducerGenerator generator, ServiceElement service) {
        super.onGenerateIocProducer(generator, service);
        TeleSchemeBuilderElement teleScheme = getTeleScheme();
        if (teleScheme == null) {
            return;
        }
    }
}
