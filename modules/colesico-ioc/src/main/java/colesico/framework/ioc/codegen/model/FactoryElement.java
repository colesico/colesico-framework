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

package colesico.framework.ioc.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.MethodElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
abstract public class FactoryElement {

    protected final ClassType suppliedType;
    protected final String factoryMethodBaseName;
    protected final ScopeElement scope;
    protected final Boolean polyproduce;

    protected final PPLDefinitionElement postProduce;
    protected final String named;
    protected final ClassType classed;

    protected final boolean notifyPostProduce;
    protected final boolean notifyPostConstruct;

    protected final List<MethodElement> postConstructListeners;

    protected final List<InjectableElement> parameters = new ArrayList<>();

    protected int factoryIndex = -1;

    public FactoryElement(ClassType suppliedType,
                          String factoryMethodBaseName,
                          ScopeElement scope,
                          Boolean polyproduce,
                          PPLDefinitionElement postProduce,
                          String named,
                          ClassType classed,
                          boolean notifyPostProduce,
                          boolean notifyPostConstruct,
                          List<MethodElement> postConstructListeners
    ) {
        this.suppliedType = suppliedType;
        this.factoryMethodBaseName = factoryMethodBaseName;
        this.scope = scope;
        this.polyproduce = polyproduce;
        this.postProduce = postProduce;
        this.named = named;
        this.classed = classed;
        this.notifyPostProduce = notifyPostProduce;
        this.notifyPostConstruct = notifyPostConstruct;
        this.postConstructListeners = postConstructListeners;
    }

    public void addParameter(InjectableElement paramElm) {
        this.parameters.add(paramElm);
    }

    public final String getFactoryMethodName() {
        return factoryMethodBaseName + "Factory" + factoryIndex;
    }

    public void setFactoryIndex(int factoryIndex) {
        this.factoryIndex = factoryIndex;
    }

    public ClassType getSuppliedType() {
        return suppliedType;
    }

    public String getFactoryMethodBaseName() {
        return factoryMethodBaseName;
    }

    public ScopeElement getScope() {
        return scope;
    }

    public Boolean getPolyproduce() {
        return polyproduce;
    }

    public String getNamed() {
        return named;
    }

    public ClassType getClassed() {
        return classed;
    }

    public PPLDefinitionElement getPostProduce() {
        return postProduce;
    }

    public List<InjectableElement> getParameters() {
        return parameters;
    }

    public List<MethodElement> getPostConstructListeners() {
        return postConstructListeners;
    }

    public boolean isNotifyPostConstruct() {
        return notifyPostConstruct;
    }

    public boolean isNotifyPostProduce() {
        return notifyPostProduce;
    }

    public int getFactoryIndex() {
        return factoryIndex;
    }
}
