package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.model.VarElement;
import colesico.framework.service.BeanField;
import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * Represents field of param bean and corresponding method parameter
 *
 * @see BeanField
 */
public class TeleBeanFieldElement extends TeleParameterElement {

    /**
     * Parent bean reference
     */
    private TeleBeanElement parentBean;

    /**
     * Bean field name
     */
    private String name;

    public TeleBeanFieldElement(TeleCommandElement parentTeleCommand, VarElement originParameter) {
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

    public TeleBeanElement parentBean() {
        return parentBean;
    }

    public void setParentBean(TeleBeanElement parentBean) {
        this.parentBean = parentBean;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
