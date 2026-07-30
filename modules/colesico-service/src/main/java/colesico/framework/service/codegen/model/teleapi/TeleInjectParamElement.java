package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

/**
 * @see colesico.framework.service.InjectParam
 */
public class TeleInjectParamElement extends TeleParameterElement {

    public TeleInjectParamElement(TeleCommandElement parentTeleCommand, VarElement originVariable) {
        super(parentTeleCommand, originVariable);
    }
}
