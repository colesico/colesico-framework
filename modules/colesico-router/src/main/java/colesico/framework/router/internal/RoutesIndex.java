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

package colesico.framework.router.internal;

import colesico.framework.router.RouterException;
import colesico.framework.router.assist.RouteTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public class RoutesIndex {

    private final Logger logger = LoggerFactory.getLogger(RoutesIndex.class);

    protected Map<String, RouteTrie.Node> nodesMap = new HashMap<>();

    public void addNode(String routeId, RouteTrie.Node node) {
        RouteTrie.Node oldNode = nodesMap.put(routeId, node);
        if (oldNode != null) {
            logger.warn("Duplicate route id: " + routeId);
        }
    }

    public List<String> getSlicedRoute(String routeId, Map<String, String> parameters) {
        RouteTrie.Node node = nodesMap.get(routeId);
        if (node == null) {
            return null;
        }
        List<String> segments = new ArrayList();
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        while ((node != null) && (node.getParent() != null)) {
            String segmentName;
            if (node.isParameter()) {
                segmentName = parameters.get(node.getName());
                if (segmentName == null) {
                    throw new RouterException("Undetermined value of route parameter :"+node.getName()+" for routeId: "+routeId);
                }
            } else {
                segmentName = node.getName();
            }
            segments.add(0, segmentName);
            node = node.getParent();
        }
        return segments;
    }
}
