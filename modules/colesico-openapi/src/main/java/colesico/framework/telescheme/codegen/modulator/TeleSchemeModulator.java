package colesico.framework.telescheme.codegen.modulator;

import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.modulator.Modulator;
import colesico.framework.telescheme.codegen.generator.TeleSchemeGenerator;
import colesico.framework.telescheme.codegen.model.TeleSchemeElement;
import com.squareup.javapoet.CodeBlock;

abstract public class TeleSchemeModulator extends Modulator {

    abstract protected boolean isTeleFacadeSupported(TeleFacadeElement teleFacade);

    abstract protected Class<?> getTeleFacadeSchemeType();

    abstract protected Class<?> getTeleMethodSchemeType();

    abstract protected CodeBlock generateTeleFacadeScheme(TeleFacadeElement teleFacade);

    /**
     * Called to process tele method after parsing completed.
     * Override this method to custom processing.
     */
    abstract protected CodeBlock generateTeleMethodScheme(TeleMethodElement teleMethod);

    protected TeleSchemeElement getTeleScheme() {
        if (service.getTeleFacade() == null) {
            return null;
        }
        return service.getTeleFacade().getProperty(TeleSchemeElement.class);
    }

    @Override
    public void onBeforeParseTeleFacade(TeleFacadeElement teleFacade) {
        super.onBeforeParseTeleFacade(teleFacade);
        if (!isTeleFacadeSupported(teleFacade)) {
            return;
        }
        TeleSchemeElement facadeScheme = new TeleSchemeElement(teleFacade, getTeleFacadeSchemeType());
        teleFacade.setProperty(TeleSchemeElement.class, facadeScheme);
    }

    @Override
    public void onTeleMethodParsed(TeleMethodElement teleMethod) {
        super.onTeleMethodParsed(teleMethod);
        if (!isTeleFacadeSupported(teleMethod.getParentTeleFacade())) {
            return;
        }

        CodeBlock ms = generateTeleMethodScheme(teleMethod);
        //TODO
    }

    @Override
    public void onTeleFacadeParsed(TeleFacadeElement teleFacade) {
        super.onTeleFacadeParsed(teleFacade);
        if (!isTeleFacadeSupported(teleFacade)) {
            return;
        }

        CodeBlock fs = generateTeleFacadeScheme(teleFacade);
        getTeleScheme().setSchemeCode(fs);
    }

    @Override
    public void onServiceGenerated(ServiceElement service) {
        super.onServiceGenerated(service);
        TeleSchemeElement teleScheme = getTeleScheme();
        if (teleScheme == null) {
            return;
        }
        TeleSchemeGenerator teleSchemeGenerator = new TeleSchemeGenerator(getProcessorContext().getProcessingEnv());
        teleSchemeGenerator.generate(teleScheme);
    }

    @Override
    public void onGenerateIocProducer(ProducerGenerator generator, ServiceElement service) {
        super.onGenerateIocProducer(generator, service);
        TeleSchemeElement teleScheme = getTeleScheme();
        if (teleScheme == null) {
            return;
        }
    }
}
