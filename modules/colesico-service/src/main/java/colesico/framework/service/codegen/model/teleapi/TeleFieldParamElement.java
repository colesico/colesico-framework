package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.BundleParam;
import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * Represents field of request bean and corresponding method parameter
 *
 * @see BundleParam
 */
public class TeleFieldParamElement extends TeleParameterElement {

    /**
     * Parent bean reference
     */
    private TeleParamBundleElement parentBean;

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

    public TeleParamBundleElement parentBean() {
        return parentBean;
    }

    public void setParentBean(TeleParamBundleElement parentBean) {
        this.parentBean = parentBean;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
