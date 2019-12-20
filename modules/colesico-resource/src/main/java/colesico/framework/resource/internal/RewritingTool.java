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
