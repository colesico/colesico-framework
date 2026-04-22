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
     * Parent tele-method ref
     */
    private final TeleMethodElement parentTeleMethod;


    protected TRContextElement readingContext;

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

    public TeleBatchElement(TeleMethodElement parentTeleMethod, String name) {
        this.parentTeleMethod = parentTeleMethod;
        this.name = name;
    }

    public void addField(TeleBatchFieldElement field) {
        fields.add(field);
        field.setParentBatch(this);
    }

    public String getBatchClassSimpleName() {
        return StrUtils.firstCharToUpperCase(parentTeleMethod.getName()) + StrUtils.firstCharToUpperCase(name);
    }

    public String getBatchClassName() {
        return parentPack.getParentTeleFacade().getParentService().getOriginClass().getPackageName() + '.' +
                parentPack.getBatchPackClassSimpleName() + '.' +
                getBatchClassSimpleName();
    }

    /**
     * Batch variable name
     */
    public String getBatchVarName() {
        return StrUtils.firstCharToLowerCase(name) + BATCH_VAR_SUFFIX;
    }

    public TeleBatchPackElement getParentPack() {
        return parentPack;
    }

    public void setParentPack(TeleBatchPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public String getName() {
        return name;
    }

    public List<TeleBatchFieldElement> getFields() {
        return fields;
    }

    @Override
    public TRContextElement getReadingContext() {
        return readingContext;
    }

    @Override
    public void setReadingContext(TRContextElement readingContext) {
        this.readingContext = readingContext;
    }
}
