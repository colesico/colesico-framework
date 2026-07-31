package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StringUtils;
import colesico.framework.service.BeanField;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent composition bean that is read from a data-port as single object
 * and its field values then assigning to the method parameters on invocation.
 *
 * @see BeanField
 */
public class TeleCompositionElement implements TeleReadableElement {

    private static final String COMPOSITION_VAR_SUFFIX = "ParamBean";

    /**
     * Parent tele-command ref
     */
    private final TeleCommandElement parentTeleCommand;

    /**
     * Pack ref
     */
    protected TeleCompositionsPackElement parentPack;

    /**
     * Composition bean name
     * Used for building bean class name
     */
    protected final String name;

    protected final List<TeleCompositionFieldElement> fields = new ArrayList<>();

    /**
     * Read bean spec
     */
    protected TeleReadElement readSpec;

    public TeleCompositionElement(TeleCommandElement parentTeleCommand, String name) {
        this.parentTeleCommand = parentTeleCommand;
        this.name = name;
    }

    public void addField(TeleCompositionFieldElement field) {
        fields.add(field);
        field.setParentBean(this);
    }

    public String compositionClassSimpleName() {
        return StringUtils.firstCharToUpperCase(parentTeleCommand.targetMethodName()) + StringUtils.firstCharToUpperCase(name);
    }

    public String compositionClassName() {
        return parentPack.parentTeleFacade().parentService().originClass().packageName() + '.' +
                parentPack.packClassSimpleName() + '.' +
                compositionClassSimpleName();
    }

    /**
     * Composition variable name
     */
    public String compositionVarName() {
        return StringUtils.firstCharToLowerCase(name) + COMPOSITION_VAR_SUFFIX;
    }

    public TeleCompositionsPackElement parentPack() {
        return parentPack;
    }

    public void setParentPack(TeleCompositionsPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public String name() {
        return name;
    }

    public List<TeleCompositionFieldElement> fields() {
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
