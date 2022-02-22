package colesico.framework.service.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class TeleBatchPackElement {
    public static final String BATCH_PACK_CLASS_SUFFIX = "BatchPack";

    private final TeleFacadeElement originTeleFacade;

    private List<TeleBatchElement> batches = new ArrayList<>();

    public TeleBatchPackElement(TeleFacadeElement originTeleFacade) {
        this.originTeleFacade = originTeleFacade;
    }

    public boolean isEmpty() {
        return batches.isEmpty();
    }

    public void addBatch(TeleBatchElement batch) {
        batches.add(batch);
        batch.setParentPack(this);
    }

    public TeleFacadeElement getOriginTeleFacade() {
        return originTeleFacade;
    }

    public List<TeleBatchElement> getBatches() {
        return batches;
    }

    public String getBatchPackClassSimpleName() {
        return originTeleFacade.getParentService().getOriginClass().getSimpleName() + BATCH_PACK_CLASS_SUFFIX;
    }
}