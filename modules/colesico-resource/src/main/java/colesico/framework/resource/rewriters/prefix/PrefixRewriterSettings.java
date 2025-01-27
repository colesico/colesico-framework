package colesico.framework.resource.rewriters.prefix;

import colesico.framework.resource.internal.rewriters.PrefixRewriter;

/**
 * Settings for {@link PrefixRewriter}.
 * It rewrites the path by a partial prefix match.
 * E.g for the rewriting  '/etc/srv'->'/foo'   path '/etc/srv/generator/x' will be rewritten to '/foo/generator/x'
 */
public interface PrefixRewriterSettings {
    PrefixRewriterSettings rewriting(String originPathPrefix, String targetPathPrefix);
}
