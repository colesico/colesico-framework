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
 * Tele-command representation.
 * Tele command is an executable command which invoke taget service method
 *
 * @see colesico.framework.teleapi.TeleCommand
 */
public final class TeleCommandElement {

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
    private final List<TeleInputElement> parameters = new ArrayList<>();

    /**
     * Parameter batches.
     * <p>
     * batch name -> batch element
     */
    private final Map<String, TeleBatchElement> batches = new HashMap<>();

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
    private final Map<Class<?>, Object> properties = new HashMap<>();

    public TeleCommandElement(ServiceMethodElement serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public <C> C property(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public <C> void setProperty(Class<C> propertyClass, C property) {
        properties.put(propertyClass, property);
    }

    /**
     * Add parameter of tele-command
     */
    public void addParameter(TeleInputElement parameter) {
        parameters.add(parameter);
    }

    /**
     * Return target service method name
     */
    public String targetMethodName() {
        return serviceMethod.name();
    }

    public String factoryMethodName() {
        return "" + StrUtils.firstCharToLowerCase(serviceMethod.name()) + "TCF" + index;
    }

    public TeleBatchElement getOrCreateBatch(String name) {
        TeleBatchElement batch = batches.get(name);
        if (batch == null) {
            batch = new TeleBatchElement(this, name);
            batches.put(name, batch);
            parentTeleFacade.batchPack().addBatch(batch);
        }
        return batch;
    }

    public ServiceMethodElement serviceMethod() {
        return serviceMethod;
    }

    public TeleFacadeElement parentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleInputElement> parameters() {
        return parameters;
    }

    public TWContextElement writingContext() {
        return writingContext;
    }

    public void setWritingContext(TWContextElement writingContext) {
        this.writingContext = writingContext;
    }

    public TIContextElement invocationContext() {
        return invocationContext;
    }

    public void setInvocationContext(TIContextElement invocationContext) {
        this.invocationContext = invocationContext;
    }

    public Integer index() {
        return index;
    }

    public Map<String, TeleBatchElement> batches() {
        return batches;
    }

    @Override
    public String toString() {
        return "TeleCommandElement{" +
                "proxyMethod=" + serviceMethod +
                '}';
    }
}

