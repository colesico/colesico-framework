package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.service.BeanField;

/**
 * Represents field of param composition bean and corresponding method parameter
 *
 * @see BeanField
 */
public class TeleCompositionFieldElement extends TeleParameterElement {

    /**
     * Parent bean reference
     */
    private TeleCompositionElement parentBean;

    /**
     * Bean field name
     */
    private String name;

    public TeleCompositionFieldElement(TeleCommandElement parentTeleCommand, VarElement originParameter) {
        super(parentTeleCommand, originParameter);
        this.name = name;
    }

    private void checkFieldName(String name) {
        //TODO
    }

    public String getterName() {
        return "get" + StringUtils.firstCharToUpperCase(name());
    }

    public String setterName() {
        return "set" + StringUtils.firstCharToUpperCase(name());
    }

    public TeleCompositionElement parentBean() {
        return parentBean;
    }

    public void setParentBean(TeleCompositionElement parentBean) {
        this.parentBean = parentBean;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
