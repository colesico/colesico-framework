/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.resource.rewriters;

import colesico.framework.resource.ResourceException;
import colesico.framework.resource.PathRewriter;
import colesico.framework.resource.RewritingPhase;
import colesico.framework.resource.assist.PathTrie;
import org.apache.commons.lang3.StringUtils;

/**
 * Rewrites the path by a partial prefix match.
 * E.g for the rewriting  '/etc/srv'->'/foo'   path '/etc/srv/generator/x' will be rewritten to '/foo/generator/x'
 */
public class PrefixRewriter implements PathRewriter {

    private final PathTrie<Rewriting> pathTrie = new PathTrie<>("/");

    public static PrefixRewriter of(String... nameValue) {
        PrefixRewriter rewriter = new PrefixRewriter();
        for (int i = 0; i < nameValue.length; i = i + 2) {
            rewriter.rewriting(nameValue[i], nameValue[i + 1]);
        }
        return rewriter;
    }

    @Override
    public RewritingPhase phase() {
        return RewritingPhase.EVALUATE;
    }

    @Override
    public final String rewrite(String path) {
        Rewriting rewriting = pathTrie.find(path);
        if (rewriting == null) {
            return path;
        }
        return rewriting.getTargetPrefix() + StringUtils.substring(path, rewriting.getOriginPrefixLength());
    }

    /**
     * Adds rewriting rule
     */
    public PrefixRewriter rewriting(String originPathPrefix, String targetPathPrefix) {
        PathTrie.Node<Rewriting> node = pathTrie.add(originPathPrefix);
        if (node.getValue() != null) {
            throw new ResourceException("Duplicate path rewriting: " + originPathPrefix);
        }
        Rewriting rewriting = new Rewriting(originPathPrefix.length(), targetPathPrefix);
        node.setValue(rewriting);
        return this;
    }

    /**
     * Rewriting info node
     */
    protected static final class Rewriting {

        private final int originPrefixLength;
        private final String targetPrefix;

        public Rewriting(int originPrefixLength, String targetPrefix) {
            this.originPrefixLength = originPrefixLength;
            this.targetPrefix = targetPrefix;
        }

        public String getTargetPrefix() {
            return targetPrefix;
        }

        public int getOriginPrefixLength() {
            return originPrefixLength;
        }
    }
}
