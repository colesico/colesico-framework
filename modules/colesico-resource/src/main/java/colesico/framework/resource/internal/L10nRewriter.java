package colesico.framework.resource.internal;

import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.RewritingPhase;
import colesico.framework.resource.localization.Localizer;
import jakarta.inject.Singleton;

@Singleton
public class L10nRewriter implements PathRewriter {

    private final Localizer localizer;

    public L10nRewriter(Localizer localizer) {
        this.localizer = localizer;
    }

    @Override
    public String rewrite(String path, RewritingPhase phase) {
        return localizer.localize(path);
    }

    @Override
    public RewritingPhase[] phases() {
        return new RewritingPhase[]{RewritingPhase.LOCALIZE};
    }

}
