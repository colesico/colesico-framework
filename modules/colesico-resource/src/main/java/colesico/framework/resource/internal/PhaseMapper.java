package colesico.framework.resource.internal;

import colesico.framework.resource.rewriting.PathRewriter;
import colesico.framework.resource.rewriting.RewritingPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhaseMapper {

    final Map<RewritingPhase, List<PathRewriter>> phaseMap = new HashMap<>();

    public void add(PathRewriter rewriter) {
        RewritingPhase phase = rewriter.phase();
        List<PathRewriter> phaseRewriters = phaseMap.computeIfAbsent(phase, p -> new ArrayList<>());
        phaseRewriters.add(rewriter);
    }

    public List<PathRewriter> getRewriters(RewritingPhase phase) {
        return phaseMap.get(phase);
    }

}
