package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * @see colesico.framework.service.InjectParam
 */
public class TeleInjectParamElement extends TeleParameterElement {

    public TeleInjectParamElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParam) {
        super(parentTeleCommand, serviceParam);
    }
}
