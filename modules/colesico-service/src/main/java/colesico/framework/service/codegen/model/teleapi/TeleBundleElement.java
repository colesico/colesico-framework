package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.BundleParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent bundle that is read from a data-port as single object
 * and its field values then assigning to the method parameters on invocation.
 *
 * @see BundleParam
 */
public class TeleBundleElement implements TeleReadableElement {

    private static final String BUNDLE_VAR_SUFFIX = "ParamBundle";

    /**
     * Parent tele-command ref
     */
    private final TeleCommandElement parentTeleCommand;

    /**
     * Pack ref
     */
    protected TeleBundlesPackElement parentPack;

    /**
     * Bundle name
     * Used for building bundle class name
     */
    protected final String name;

    protected final List<TeleBundleFieldElement> fields = new ArrayList<>();

    /**
     * Read bundle spec
     */
    protected TeleReadElement readSpec;

    public TeleBundleElement(TeleCommandElement parentTeleCommand, String name) {
        this.parentTeleCommand = parentTeleCommand;
        this.name = name;
    }

    public void addField(TeleBundleFieldElement field) {
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

    public TeleBundlesPackElement parentPack() {
        return parentPack;
    }

    public void setParentPack(TeleBundlesPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public String name() {
        return name;
    }

    public List<TeleBundleFieldElement> fields() {
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
