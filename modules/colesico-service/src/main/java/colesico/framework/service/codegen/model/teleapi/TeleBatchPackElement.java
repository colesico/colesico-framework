package colesico.framework.service.codegen.model.teleapi;

import java.util.ArrayList;
import java.util.List;

public class TeleBatchPackElement {

    public static final String BATCH_PACK_CLASS_SUFFIX = "BatchPack";

    private final TeleFacadeElement parentTeleFacade;

    private List<TeleBatchElement> batches = new ArrayList<>();

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

    public TeleFacadeElement getParentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleBatchElement> getBatches() {
        return batches;
    }

    public String getBatchPackClassSimpleName() {
        return parentTeleFacade.getParentService().getOriginClass().getSimpleName() + BATCH_PACK_CLASS_SUFFIX;
    }
}