/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.resource.internal;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourcePrefixOptionsPrototype;
import colesico.framework.resource.assist.PathTrie;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

/**
 * Rewrites the resource name by a partial prefix match.
 * E.g for the rewriting  '/etc/srv'->'/foo'   resource name '/etc/srv/generator/x' will be rewritten to  '/foo/generator/x'
 */
@Singleton
public class PrefixSubstitutor implements ResourcePrefixOptionsPrototype.Options {

    private final PathTrie<Rewriting> pathTrieBefore = PathTrie.of();
    private final PathTrie<Rewriting> pathTrieAfter = PathTrie.of();

    public PrefixSubstitutor(Polysupplier<ResourcePrefixOptionsPrototype> configSup) {
        configSup.forEach(conf -> conf.configure(this));
    }

    private PathTrie<Rewriting> pathTrie(ResourcePrefixOptionsPrototype.Phase phase) {
        return switch (phase) {
            case BEFORE_LOCALIZE -> pathTrieBefore;
            case AFTER_LOCALIZE -> pathTrieAfter;
        };
    }

    public final String substitutePrefix(String resourceName, ResourcePrefixOptionsPrototype.Phase phase) {
        Rewriting rewriting = pathTrie(phase).find(resourceName);
        if (rewriting == null) {
            return resourceName;
        }
        if (resourceName.charAt(0) == '/') {
            return "/" + rewriting.getTargetPrefix() + StringUtils.substring(resourceName, rewriting.getOriginPrefixLength() + 1);
        }
        return rewriting.getTargetPrefix() + StringUtils.substring(resourceName, rewriting.getOriginPrefixLength());
    }

    @Override
    public ResourcePrefixOptionsPrototype.Options substitution(String originPrefix, String targetPrefix,
                                                               ResourcePrefixOptionsPrototype.Phase phase) {
        addPrefixSubstitution(originPrefix, targetPrefix, phase);
        return this;
    }

    /**
     * Adds rewriting rule
     */
    public void addPrefixSubstitution(String originPrefix, String targetPrefix,
                                      ResourcePrefixOptionsPrototype.Phase phase) {
        PathTrie.Node<Rewriting> node = pathTrie(phase).add(originPrefix);
        if (node.getValue() != null) {
            throw new ResourceException("Duplicate resource name prefix substitution: " + originPrefix);
        }
        String[] targetParts = StringUtils.split(targetPrefix, "/");
        targetPrefix = StringUtils.joinWith("/", targetParts);
        Rewriting rewriting = new Rewriting(originPrefix.length(), targetPrefix);
        node.setValue(rewriting);
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
