package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.FieldParam;
import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * Represents field of request bean and corresponding method parameter
 *
 * @see FieldParam
 */
public class TeleFieldParamElement extends TeleParameterElement {

    /**
     * Parent bean reference
     */
    private TeleRequestBeanElement parentBean;

    /**
     * Request bean field name
     */
    private String name;

    public TeleFieldParamElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParam, String name) {
        super(parentTeleCommand, serviceParam);
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

    public TeleRequestBeanElement parentBean() {
        return parentBean;
    }

    public void setParentBean(TeleRequestBeanElement parentBean) {
        this.parentBean = parentBean;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
