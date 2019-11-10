/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.widget;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Vladlen Larionov
 */
public interface ResourceReference {

    static final String CSS_RESOURCE_TYPE = "css";
    static final String JS_RESOURCE_TYPE = "js";

    /**
     *
     * @param resourceType
     * @param resourceId
     * @param reference
     * @param dependencies  Identifiers of resources on which the resource depends
     */
    void add(String resourceType, String resourceId, String reference, String[] dependencies);

    List<String> get(String resourceType);

    String get(String resourceType, String resourceId);

    Map<String, Map<String, String>> getMap();

    void clear();
}
