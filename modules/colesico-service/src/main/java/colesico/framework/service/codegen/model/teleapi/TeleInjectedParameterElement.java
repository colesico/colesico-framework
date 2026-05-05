package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

/**
 * @see colesico.framework.service.InjectParam
 */
public class TeleInjectedParameterElement extends TeleInputElement {

    public TeleInjectedParameterElement(TeleCommandElement parentTeleCommand, VarElement originElement) {
        super(parentTeleCommand, originElement);
    }
}
