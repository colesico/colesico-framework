package colesico.framework.resource.internal;

import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.RewritingPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhaseMapper {

    final Map<RewritingPhase, List<RewriterRef>> phaseMap = new HashMap<>();

    public void add(PathRewriter rewriter) {
        RewritingPhase[] phaseSet = rewriter.phases();
        for (RewritingPhase phase : phaseSet) {
            var phaseRewriters = phaseMap.computeIfAbsent(phase, p -> new ArrayList<>());
            phaseRewriters.add(new RewriterRef(phase, rewriter));
        }
    }

    public List<RewriterRef> getRewriters(RewritingPhase phase) {
        return phaseMap.get(phase);
    }

    public record RewriterRef(RewritingPhase phase, PathRewriter rewriter) {
        public String rewrite(String path) {
            return rewriter.rewrite(path, phase);
        }
    }
}
