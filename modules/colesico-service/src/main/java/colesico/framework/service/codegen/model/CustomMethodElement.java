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

package colesico.framework.service.codegen.model;

import com.squareup.javapoet.MethodSpec;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public final class CustomMethodElement {

    protected ServiceElement parentService;
    private final MethodSpec spec;

    /**
     * Common purpose properties
     */
    private final Map<Class, Object> properties;

    public CustomMethodElement(MethodSpec spec) {
        this.parentService = null;
        this.spec = spec;
        this.properties = new HashMap();
    }

    public MethodSpec getSpec() {
        return spec;
    }

    public String getName() {
        return spec.name;
    }

    public ServiceElement getParentService() {
        return parentService;
    }

    public <P> P getProperty(Class<P> propertyClass) {
        return (P) properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }

}
