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

package colesico.framework.service.codegen.model;

import colesico.framework.assist.StrUtils;
import com.squareup.javapoet.CodeBlock;

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
    protected TeleFacadeElement parentTeleFacade;

    /**
     * Service method reference
     */
    private final ServiceMethodElement serviceMethod;

    /**
     * Tele-method parameters
     */
    private final List<TeleArgumentElement> parameters;

    /**
     * Method result writing context code
     */
    private CodeBlock writingContextCode;

    /**
     * Invocation context code
     */
    private CodeBlock invocationContextCode;

    /**
     * Common purpose props
     */
    private final Map<Class, Object> properties;

    /**
     * Tele-method index in tele-facade
     */
    protected Integer index;

    public TeleMethodElement(ServiceMethodElement serviceMethod) {
        this.serviceMethod = serviceMethod;
        this.parameters = new ArrayList<>();
        this.properties = new HashMap<>();
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    /**
     * Add parameter of tele-method
     */
    public void addParameter(TeleArgumentElement param) {
        parameters.add(param);
        param.setParentTeleMethod(this);
    }

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

    public List<TeleArgumentElement> getParameters() {
        return parameters;
    }

    public CodeBlock getWritingContextCode() {
        return writingContextCode;
    }

    public void setWritingContextCode(CodeBlock writingContextCode) {
        this.writingContextCode = writingContextCode;
    }

    public CodeBlock getInvocationContextCode() {
        return invocationContextCode;
    }

    public void setInvocationContextCode(CodeBlock invocationContextCode) {
        this.invocationContextCode = invocationContextCode;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "TeleMethodElement{" +
                "proxyMethod=" + serviceMethod +
                '}';
    }
}

