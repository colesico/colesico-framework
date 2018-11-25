package colesico.framework.resource.internal;

import colesico.framework.resource.ResourceException;
import colesico.framework.resource.assist.PathTrie;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;

@Singleton
public class RewritingTool {
    private final PathTrie<RewritingInfo> pathTrie = new PathTrie<>("/");

    public RewritingTool() {

    }

    public void addRewriting(String originPathPrefix, String targetPathPrefix) {
        PathTrie.Node<RewritingInfo> node = pathTrie.add(originPathPrefix);
        if (node.getValue() != null) {
            throw new ResourceException("Duplicate path rewriting: " + originPathPrefix);
        }
        RewritingInfo rewritingInfo = new RewritingInfo(originPathPrefix.length(), targetPathPrefix);
        node.setValue(rewritingInfo);
    }

    public final String rewrite(String resourcePath) {
        RewritingInfo rewritingInfo = pathTrie.find(resourcePath);
        if (rewritingInfo == null) {
            return resourcePath;
        }
        return rewritingInfo.getTargetPrefix() + StringUtils.substring(resourcePath, rewritingInfo.getOriginPrefixLength());
    }

}
