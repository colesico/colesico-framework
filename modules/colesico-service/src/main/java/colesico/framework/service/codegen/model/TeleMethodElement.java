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
 * @author Vladlen Larionov
 */
public final class TeleMethodElement {

    protected TeleFacadeElement parentTeleFacade;

    /**
     * Method of a service proxy. This can be used to access origin service method.
     */
    private final ProxyMethodElement proxyMethod;

    /**
     * Tele-method parameters
     */
    private final List<TeleVarElement> parameters;

    //  writing result context
    private CodeBlock writingContext;
    // invoke service method context
    private CodeBlock invokingContext;

    private final Map<Class, Object> properties;

    /**
     * Tele-method index in tele-facade
     */
    protected Integer index;

    public TeleMethodElement(ProxyMethodElement proxyMethod) {
        this.proxyMethod = proxyMethod;
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
     *
     * @param param
     */
    public void addParameter(TeleVarElement param) {
        linkVariable(param);
        parameters.add(param);
    }

    /**
     * Associate tele-var element with this tele-metod
     */
    public void linkVariable(TeleVarElement var) {
        var.parentTeleMethod = this;
    }

    public String getName() {
        return proxyMethod.getName();
    }

    public String getBuilderName() {
        return "get" + StrUtils.firstCharToUpperCase(proxyMethod.getName()) +"TM"+ index;
    }

    public ProxyMethodElement getProxyMethod() {
        return proxyMethod;
    }

    public TeleFacadeElement getParentTeleFacade() {
        return parentTeleFacade;
    }

    public List<TeleVarElement> getParameters() {
        return parameters;
    }

    public CodeBlock getWritingContext() {
        return writingContext;
    }

    public void setWritingContext(CodeBlock writingContext) {
        this.writingContext = writingContext;
    }

    public CodeBlock getInvokingContext() {
        return invokingContext;
    }

    public void setInvokingContext(CodeBlock invokingContext) {
        this.invokingContext = invokingContext;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "TeleMethodElement{" +
                "proxyMethod=" + proxyMethod +
                '}';
    }
}

