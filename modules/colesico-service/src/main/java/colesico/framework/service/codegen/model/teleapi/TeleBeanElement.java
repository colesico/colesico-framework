package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.BeanField;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent bean that is read from a data-port as single object
 * and its field values then assigning to the method parameters on invocation.
 *
 * @see BeanField
 */
public class TeleBeanElement implements TeleReadableElement {

    private static final String PARAM_BEAN_VAR_SUFFIX = "ParamBean";

    /**
     * Parent tele-command ref
     */
    private final TeleCommandElement parentTeleCommand;

    /**
     * Pack ref
     */
    protected TeleBeansPackElement parentPack;

    /**
     * Bean name
     * Used for building bean class name
     */
    protected final String name;

    protected final List<TeleBeanFieldElement> fields = new ArrayList<>();

    /**
     * Read bean spec
     */
    protected TeleReadElement readSpec;

    public TeleBeanElement(TeleCommandElement parentTeleCommand, String name) {
        this.parentTeleCommand = parentTeleCommand;
        this.name = name;
    }

    public void addField(TeleBeanFieldElement field) {
        fields.add(field);
        field.setParentBean(this);
    }

    public String paramBeanClassSimpleName() {
        return StringUtils.firstCharToUpperCase(parentTeleCommand.targetMethodName()) + StringUtils.firstCharToUpperCase(name);
    }

    public String paramBeanClassName() {
        return parentPack.parentTeleFacade().parentService().originClass().packageName() + '.' +
                parentPack.packClassSimpleName() + '.' +
                paramBeanClassSimpleName();
    }

    /**
     * ParamBean variable name
     */
    public String paramBeanVarName() {
        return StringUtils.firstCharToLowerCase(name) + PARAM_BEAN_VAR_SUFFIX;
    }

    public TeleBeansPackElement parentPack() {
        return parentPack;
    }

    public void setParentPack(TeleBeansPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public String name() {
        return name;
    }

    public List<TeleBeanFieldElement> fields() {
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
