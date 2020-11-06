package colesico.framework.resource.rewriters;

import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.RewritingPhase;

import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple localization rewriter
 */
public class LocalizationRewriter implements PathRewriter {

    private final Provider<Locale> localeProv;

    public LocalizationRewriter(Provider<Locale> localeProv) {
        this.localeProv = localeProv;
    }

    @Override
    public RewritingPhase phase() {
        return RewritingPhase.LOCALIZE;
    }

    @Override
    public String rewrite(String path) {
        // TODO: implement
        ResourceBundle rb = ResourceBundle.getBundle(path, localeProv.get());
        return null;
    }
}
