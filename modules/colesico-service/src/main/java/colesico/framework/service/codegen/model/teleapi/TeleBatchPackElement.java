package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleBatchPackElement {

    public static final String BATCH_PACK_CLASS_SUFFIX = "BatchPack";

    private final TeleFacadeElement parentTeleFacade;

    private final List<TeleBatchElement> batches = new ArrayList<>();

    public TeleBatchPackElement(TeleFacadeElement parentTeleFacade) {
        this.parentTeleFacade = parentTeleFacade;
    }

    public boolean isEmpty() {
        return batches.isEmpty();
    }

    public void addBatch(TeleBatchElement batch) {
        batches.add(batch);
        batch.setParentPack(this);
    }

    public TeleFacadeElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleBatchElement> batches() {
        return batches;
    }

    public String batchPackClassSimpleName() {
        return parentTeleFacade.parentService().originClass().getSimpleName() + BATCH_PACK_CLASS_SUFFIX;
    }
}
