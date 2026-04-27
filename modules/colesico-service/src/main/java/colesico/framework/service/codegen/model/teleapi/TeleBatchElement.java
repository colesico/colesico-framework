package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent parameter batch class.
 * A parameter batch is an object that is read from a data-port and its
 * field values will  be assigned to the method parameters when it is invoked.
 *
 * @see colesico.framework.service.BatchField
 */
public class TeleBatchElement implements TeleReadableElement {

    private static final String BATCH_VAR_SUFFIX = "Batch";

    /**
     * Parent tele-command ref
     */
    private final TeleCommandElement parentTeleCommand;


    protected TRContextElement readContext;

    /**
     * Batch pack ref
     */
    protected TeleBatchPackElement parentPack;

    /**
     * Batch name
     * Used for building batch class name
     */
    protected final String name;

    protected final List<TeleBatchFieldElement> fields = new ArrayList<>();

    public TeleBatchElement(TeleCommandElement parentTeleCommand, String name) {
        this.parentTeleCommand = parentTeleCommand;
        this.name = name;
    }

    public void addField(TeleBatchFieldElement field) {
        fields.add(field);
        field.setParentBatch(this);
    }

    public String batchClassSimpleName() {
        return StrUtils.firstCharToUpperCase(parentTeleCommand.name()) + StrUtils.firstCharToUpperCase(name);
    }

    public String batchClassName() {
        return parentPack.parentTeleFacade().parentService().originClass().packageName() + '.' +
                parentPack.batchPackClassSimpleName() + '.' +
                batchClassSimpleName();
    }

    /**
     * Batch variable name
     */
    public String batchVarName() {
        return StrUtils.firstCharToLowerCase(name) + BATCH_VAR_SUFFIX;
    }

    public TeleBatchPackElement parentPack() {
        return parentPack;
    }

    public void setParentPack(TeleBatchPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public String name() {
        return name;
    }

    public List<TeleBatchFieldElement> fields() {
        return fields;
    }

    @Override
    public TRContextElement readContext() {
        return readContext;
    }

    @Override
    public void setReadContext(TRContextElement readContext) {
        this.readContext = readContext;
    }
}
