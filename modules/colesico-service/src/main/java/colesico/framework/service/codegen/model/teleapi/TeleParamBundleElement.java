package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.BundleParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent bean that is read from a data-port as single object
 * and its field values then assigning to the method parameters on invocation.
 *
 * @see BundleParam
 */
public class TeleParamBundleElement implements TeleReadableElement {

    private static final String BUNDLE_VAR_SUFFIX = "ParamBundle";

    /**
     * Parent tele-command ref
     */
    private final TeleCommandElement parentTeleCommand;

    /**
     * Pack ref
     */
    protected TeleParamBundlesPackElement parentPack;

    /**
     * Request bean name
     * Used for building bean class name
     */
    protected final String name;

    protected final List<TeleFieldParamElement> fields = new ArrayList<>();

    /**
     * Read paramBundle spec
     */
    protected TeleReadElement readSpec;

    public TeleParamBundleElement(TeleCommandElement parentTeleCommand, String name) {
        this.parentTeleCommand = parentTeleCommand;
        this.name = name;
    }

    public void addField(TeleFieldParamElement field) {
        fields.add(field);
        field.setParentBean(this);
    }

    public String paramBundleClassSimpleName() {
        return StringUtils.firstCharToUpperCase(parentTeleCommand.targetMethodName()) + StringUtils.firstCharToUpperCase(name);
    }

    public String paramBundleClassName() {
        return parentPack.parentTeleFacade().parentService().originClass().packageName() + '.' +
                parentPack.packClassSimpleName() + '.' +
                paramBundleClassSimpleName();
    }

    /**
     * ParamBundle variable name
     */
    public String paramBundleVarName() {
        return StringUtils.firstCharToLowerCase(name) + BUNDLE_VAR_SUFFIX;
    }

    public TeleParamBundlesPackElement parentPack() {
        return parentPack;
    }

    public void setParentPack(TeleParamBundlesPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public String name() {
        return name;
    }

    public List<TeleFieldParamElement> fields() {
        return fields;
    }

    @Override
    public TeleReadElement readSpec() {
        return readSpec;
    }

    @Override
    public void setReadSpec(TeleReadElement readSpec) {
        this.readSpec = readSpec;
    }
}
