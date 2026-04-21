/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.ServiceMethodElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tele-method representation
 */
public final class TeleMethodElement {

    /**
     * Parent tele-facade ref
     */
    TeleFacadeElement parentTeleFacade;

    /**
     * Service method reference
     */
    private final ServiceMethodElement serviceMethod;

    /**
     * Method parameters
     */
    private final List<TeleParameterElement> parameters;

    /**
     * Tele-method index within tele-facade
     */
    Integer index;

    /**
     * Method result writing context
     */
    private TWContextElement writingContext;

    /**
     * Invocation context code
     */
    private TIContextElement invocationContext;

    /**
     * Common purpose props
     */
    private final Map<Class<?>, ?> properties;

    public TeleMethodElement(ServiceMethodElement serviceMethod) {
        this.serviceMethod = serviceMethod;
        this.parameters = new ArrayList<>();
        this.properties = new HashMap<>();
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public <C> void setProperty(Class<C> propertyClass, C property) {
        properties.put(propertyClass, property);
    }

    /**
     * Add parameter of tele-method
     */
    public void addParameter(TeleParameterElement param) {
        parameters.add(param);
    }

    /**
     * Return origin service method name
     */
    public String getName() {
        return serviceMethod.getName();
    }

    public String getBuilderName() {
        return "get" + StrUtils.firstCharToUpperCase(serviceMethod.getName()) + "TM" + index;
    }

    public ServiceMethodElement getServiceMethod() {
        return serviceMethod;
    }

    public TeleFacadeElement getParentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleParameterElement> getParameters() {
        return parameters;
    }

    public TWContextElement getWritingContext() {
        return writingContext;
    }

    public void setWritingContext(TWContextElement writingContext) {
        this.writingContext = writingContext;
    }

    public TIContextElement getInvocationContext() {
        return invocationContext;
    }

    public void setInvocationContext(TIContextElement invocationContext) {
        this.invocationContext = invocationContext;
    }

    public Integer getIndex() {
        return index;
    }

    public Map<String, TeleBatchElement> getBatches() {
        return batches;
    }

    @Override
    public String toString() {
        return "TeleMethodElement{" +
                "proxyMethod=" + serviceMethod +
                '}';
    }
}

