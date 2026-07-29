package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.BundleParam;
import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * Represents field of param bundle and corresponding method parameter
 *
 * @see BundleParam
 */
public class TeleBundleFieldElement extends TeleParameterElement {

    /**
     * Parent bean reference
     */
    private TeleBundleElement parentBean;

    /**
     * Bundle field name
     */
    private String name;

    public TeleBundleFieldElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParam, String name) {
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

    public TeleBundleElement parentBean() {
        return parentBean;
    }

    public void setParentBean(TeleBundleElement parentBean) {
        this.parentBean = parentBean;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
