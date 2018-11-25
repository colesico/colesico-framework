/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.resource.assist;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Vladlen Larionov
 */
public class PathTrie<V> {

    protected final Node<V> rootNode;
    protected final String delimiter;


    public PathTrie(String delimiter) {
        this.rootNode = new Node<>();
        this.delimiter = delimiter;
    }

    /**
     * Creates the path in the trie and returns the terminal node.
     * This node can be used to add value.
     *
     * @param path
     * @return
     */
    public final Node<V> add(String path) {
        final StringTokenizer pathTokenizer = new StringTokenizer(path, delimiter);
        Node<V> node = rootNode;
        while (pathTokenizer.hasMoreElements()) {
            String pathItem = pathTokenizer.nextToken();
            node = node.provideChild(pathItem);
        }
        return node;
    }

    public final V find(String path, Filter<V> filter) {
        final StringTokenizer pathTokenize = new StringTokenizer(path, delimiter);
        Node<V> currentNode = rootNode;
        Node<V> selectedNode = null;
        while (pathTokenize.hasMoreElements()) {
            final String pathItem = pathTokenize.nextToken();
            final Node child = currentNode.getChild(pathItem);
            if (child == null) {
                return selectedNode == null ? null : selectedNode.getValue();
            } else {
                currentNode = child;
                if (filter.accept(currentNode.getValue())) {
                    selectedNode = currentNode;
                }
            }
        }
        return selectedNode == null ? null : selectedNode.getValue();
    }

    public final V find(String path) {
        final StringTokenizer pathTokenize = new StringTokenizer(path, delimiter);
        Node<V> currentNode = rootNode;
        while (pathTokenize.hasMoreElements()) {
            final String pathItem = pathTokenize.nextToken();
            final Node child = currentNode.getChild(pathItem);
            if (child == null) {
                return currentNode.getValue();
            } else {
                currentNode = child;
            }
        }
        return currentNode.getValue();
    }

    public static final class Node<V> {
        private V value;
        private final Map<String, Node<V>> children = new HashMap<>();

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<V> getChild(String nodeName) {
            return children.get(nodeName);
        }

        public Node<V> provideChild(String nodeName) {
            Node<V> child = children.computeIfAbsent(nodeName, k -> new Node<>());
            return child;
        }
    }

    @FunctionalInterface
    public interface Filter<V> {
        boolean accept(V value);
    }
}
