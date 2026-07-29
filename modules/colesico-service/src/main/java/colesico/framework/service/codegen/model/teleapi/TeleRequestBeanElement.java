package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.FieldParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent request bean class.
 * A request bean is an object that is read from a data-port as single object
 * and its field values then assigning to the method parameters on invocation.
 *
 * @see FieldParam
 */
public class TeleRequestBeanElement implements TeleReadableElement {

    private static final String REQUEST_BEAN_VAR_SUFFIX = "Batch";

    /**
     * Parent tele-command ref
     */
    private final TeleCommandElement parentTeleCommand;

    /**
     * Pack ref
     */
    protected TeleRequestBeanPackElement parentPack;

    /**
     * Request bean name
     * Used for building bean class name
     */
    protected final String name;

    protected final List<TeleFieldParamElement> fields = new ArrayList<>();

    /**
     * Read batch spec
     */
    protected TeleReadElement readSpec;

    public TeleRequestBeanElement(TeleCommandElement parentTeleCommand, String name) {
        this.parentTeleCommand = parentTeleCommand;
        this.name = name;
    }

    public void addField(TeleFieldParamElement field) {
        fields.add(field);
        field.setParentBean(this);
    }

    public String batchClassSimpleName() {
        return StringUtils.firstCharToUpperCase(parentTeleCommand.targetMethodName()) + StringUtils.firstCharToUpperCase(name);
    }

    public String batchClassName() {
        return parentPack.parentTeleFacade().parentService().originClass().packageName() + '.' +
                parentPack.packClassSimpleName() + '.' +
                batchClassSimpleName();
    }

    /**
     * Batch variable name
     */
    public String batchVarName() {
        return StringUtils.firstCharToLowerCase(name) + REQUEST_BEAN_VAR_SUFFIX;
    }

    public TeleRequestBeanPackElement parentPack() {
        return parentPack;
    }

    public void setParentPack(TeleRequestBeanPackElement parentPack) {
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
