package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.service.codegen.model.ServiceParameterElement;

public class TeleCompositeParamElement extends TeleParameterElement {

    private final TeleCompositeElement composite;

    public TeleCompositeParamElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParameter, TeleCompositeElement composite) {
        super(parentTeleCommand, serviceParameter);
        this.composite = composite;
    }

    public TeleCompositeElement composite() {
        return composite;
    }

}
