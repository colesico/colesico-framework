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

import com.squareup.javapoet.CodeBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public final class TeleMethodElement {

    protected TeleFacadeElement parentTeleFacade;
    // tele-method name
    private final String name;
    // service method reference
    private final ProxyMethodElement proxyMethod;
    // tele-method parameters
    private final List<TeleVarElement> parameters;

    //  writing result context
    private CodeBlock writingContext;
    // invoke service method context
    private CodeBlock invokingContext;

    public TeleMethodElement(String name, ProxyMethodElement proxyMethod) {
        this.name = name;
        this.proxyMethod = proxyMethod;
        this.parameters = new ArrayList<>();
    }

    /**
     * Add parameter of tele-method
     * @param param
     */
    public void addParameter(TeleVarElement param) {
        linkVariable(param);
        parameters.add(param);
    }

    /**
     * Associate tele-var element with this tele-metod
     * @param var
     */
    public void linkVariable(TeleVarElement var) {
        var.parentTeleMethod = this;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return "TeleMethodElement{" +
                "name='" + name + '\'' +
                ", proxyMethod=" + proxyMethod +
                '}';
    }
}

