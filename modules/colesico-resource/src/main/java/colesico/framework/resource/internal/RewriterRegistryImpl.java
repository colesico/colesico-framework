package colesico.framework.resource.internal;

import colesico.framework.resource.*;

import java.util.*;

public class RewriterRegistryImpl implements RewriterRegistry {

    private Map<String, RewriterRegistry.Entry> entries = new LinkedHashMap<>();
    final Map<RewritingPhase, List<PathRewriter>> phaseMap = new HashMap<>();


    @Override
    public void register(String id, PathRewriter rewriter, RewritingPhase phase) {
        if (entries.get(id) != null) {
            throw new ResourceException("Rewriter already registered. ID=" + id);
        }
        RewriterRegistry.Entry entry = new RewriterRegistry.Entry(rewriter, phase);
        entries.put(id, entry);
        List<PathRewriter> phaseRewriters = phaseMap.computeIfAbsent(phase, p -> new ArrayList<>());
        phaseRewriters.add(rewriter);
    }

    @Override
    public void registerIfAbsent(String id, PathRewriter rewriter, RewritingPhase phase) {
        if (entries.get(id) == null) {
            register(id, rewriter, phase);
        }
    }

    @Override
    public void register(PathRewriter rewriter, RewritingPhase phase) {
        String id = UUID.randomUUID().toString();
        register(id, rewriter, phase);
    }

    @Override
    public RewriterRegistry.Entry get(String id) {
        return entries.get(id);
    }

    public List<PathRewriter> getPhaseRewriters(RewritingPhase phase) {
        return phaseMap.get(phase);
    }
}
