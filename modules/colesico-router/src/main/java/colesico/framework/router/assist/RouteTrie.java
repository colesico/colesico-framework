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

package colesico.framework.router.assist;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Vladlen Larionov
 */
public class RouteTrie<V> {

    public static final String SEGMENT_DELEMITER = "/";
    public static final String PARAM_PREFIX = ":";
    public static final String SUFFIX_PARAM_MARKER = "*";
    public static final String SUFFIX_PARAM_NAME = "routeSuffix";

    protected final Node<V> rootNode = new RouteTrie.Node(null, null, false);

    public RouteTrie(V rootNodeValue) {
        this.rootNode.setValue(rootNodeValue);
    }

    public Node<V> addRoute(String route, V value) {
        StringTokenizer routeTokenizer = new StringTokenizer(route, SEGMENT_DELEMITER);
        Node<V> node = rootNode;
        while (routeTokenizer.hasMoreElements()) {
            String routeItem = routeTokenizer.nextToken();
            if (routeItem.startsWith(PARAM_PREFIX)) {
                routeItem = routeItem.substring(PARAM_PREFIX.length());
                node = node.addParameter(routeItem);
            } else if (routeItem.equals(SUFFIX_PARAM_MARKER)) {
                node = node.addParameter(SUFFIX_PARAM_NAME);
                break;
            } else {
                node = node.addSegment(routeItem);
            }
        }
        if (node.getValue() != null) {
            throw new DuplicateRouteException(route);
        }
        node.setValue(value);
        return node;
    }


    public RouteResolution<V> resolveRoute(String route) {
        StringTokenizer routeTokenize = new StringTokenizer(route, SEGMENT_DELEMITER);
        Node<V> node = rootNode;
        Map<String, String> params = new HashMap<>();
        int routeOffset = 0;
        boolean proceedRoute = true;
        while (routeTokenize.hasMoreElements() && proceedRoute) {
            String routeItem = routeTokenize.nextToken();
            Node<V> child = node.getSegment(routeItem);
            if (child == null) {
                child = node.getParameter();
                if (child == null) {
                    return null;
                }

                if (child.getName().equals(SUFFIX_PARAM_NAME)) {
                    routeItem = route.substring(routeOffset);
                    proceedRoute = false;
                }

                params.put(child.getName(), routeItem);
            }
            node = child;
            routeOffset += routeItem.length() + 1;
        }
        return new RouteResolution<>(node, params);
    }

    public Node<V> getRootNode() {
        return rootNode;
    }

    public static class RouteResolution<V> {
        private final Node<V> node;
        private final Map<String, String> params;

        public RouteResolution(Node<V> node, Map<String, String> params) {
            this.node = node;
            this.params = params;
        }

        public Node<V> getNode() {
            return node;
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    public static class DuplicateRouteException extends RuntimeException {
        private final String route;

        public DuplicateRouteException(String route) {
            super("Duplicate route: "+route);
            this.route = route;
        }

        public String getRoute() {
            return route;
        }
    }


    public static class RouteParameterMismutch extends RuntimeException {
        private final String parameter;

        public RouteParameterMismutch(String parameter, String existingParameter) {
            super("Route parameter mismutch: " + parameter + ". It overrides the previously registered: " + existingParameter);
            this.parameter = parameter;
        }

        public String getParameter() {
            return parameter;
        }
    }

    public static class Node<V> {
        // Parent route node ref
        protected final Node<V> parent;

        // Node name  (segment name or a parameter name)
        protected final String name;

        // Node is a parameter, is a segment otherwise
        protected final boolean isParameter;

        // Node payload
        protected V value;

        // Child segments nodes refs
        protected final Map<String, Node<V>> segments = new HashMap();
        // Child parameter node ref
        protected Node<V> parameter;

        public Node(Node<V> parent, String name, boolean isParameter) {
            this.parent = parent;
            this.name = name;
            this.isParameter = isParameter;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Adds child node as a segment
         * @param name
         * @return
         */
        public Node<V> addSegment(String name) {
            Node<V> child = segments.computeIfAbsent(name, nodeName -> new Node<>(this, nodeName, false));
            return child;
        }

        /**
         * Add child node as a parameter
         * @param name
         * @return
         */
        public Node<V> addParameter(String name) {
            if (this.parameter == null) {
                this.parameter = new Node<>(this, name, true);
            } else {
                if (!this.parameter.getName().equals(name)) {
                    throw new RouteParameterMismutch(name, this.parameter.getName());
                }
            }
            return this.parameter;
        }

        /**
         * Return child segment node
         * @param nodeName
         * @return
         */
        public Node<V> getSegment(String nodeName) {
            return this.segments.get(nodeName);
        }

        /**
         * Return child parameter node
         * @return
         */
        public Node<V> getParameter() {
            return this.parameter;
        }

        public Node<V> getParent() {
            return this.parent;
        }

        public Node<V> getRoot() {
            Node<V> root = this;
            while (root.parent != null) {
                if (root.parent.parent==null){
                    return root;
                }
                root = root.parent;
            }
            return root;
        }

        public String getName() {
            return name;
        }

        public boolean isParameter() {
            return isParameter;
        }
    }
}
