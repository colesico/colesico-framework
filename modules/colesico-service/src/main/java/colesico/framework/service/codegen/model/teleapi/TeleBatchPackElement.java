package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleBatchPackElement {

    public static final String BATCH_PACK_CLASS_SUFFIX = "BatchPack";

    private final TeleServiceElement parentTeleFacade;

    private final List<TeleBatchElement> batches = new ArrayList<>();

    public TeleBatchPackElement(TeleServiceElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return batches.isEmpty();
    }

    public void addBatch(TeleBatchElement batch) {
        batches.add(batch);
        batch.setParentPack(this);
    }

    public TeleServiceElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleBatchElement> batches() {
        return batches;
    }

    public String batchPackClassSimpleName() {
        return parentTeleFacade.parentService().originClass().simpleName() + BATCH_PACK_CLASS_SUFFIX;
    }
}
