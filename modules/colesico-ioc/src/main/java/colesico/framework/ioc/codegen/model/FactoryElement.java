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
import colesico.framework.ioc.listener.ListenersControl;
import colesico.framework.ioc.production.KeyType;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
abstract public class FactoryElement {

    protected final ClassType suppliedType;
    protected final String factoryMethodBaseName;
    protected final ScopeElement scope;
    protected final ConditionElement condition;
    protected final SubstitutionElement substitution;

    /**
     * {@link colesico.framework.ioc.production.Polyproduce}
     */
    protected final Boolean polyproduce;

    protected final PostProduceElement postProduce;

    /**
     * {@link javax.inject.Named}
     */
    protected final String named;

    /**
     * {@link colesico.framework.ioc.production.Classed}
     */
    protected final ClassifierType classed;

    /**
     * {@link ListenersControl#postProduce()}
     */
    protected final boolean notifyPostProduce;

    /**
     * {@link ListenersControl#postProduce()}
     */
    protected final boolean notifyPostConstruct;

    protected final List<MethodElement> postConstructListeners;

    protected final List<InjectableElement> parameters = new ArrayList<>();

    protected int factoryIndex = -1;

    /**
     * {@link KeyType}
     */
    protected final List<ClassType> keyTypes = new ArrayList<>();

    public FactoryElement(ClassType suppliedType,
                          String factoryMethodBaseName,
                          ScopeElement scope,
                          ConditionElement condition,
                          SubstitutionElement substitution,
                          Boolean polyproduce,
                          PostProduceElement postProduce,
                          String named,
                          ClassifierType classed,
                          boolean notifyPostProduce,
                          boolean notifyPostConstruct,
                          List<MethodElement> postConstructListeners
    ) {
        this.suppliedType = suppliedType;
        this.factoryMethodBaseName = factoryMethodBaseName;
        this.scope = scope;
        this.condition = condition;
        this.substitution = substitution;
        this.polyproduce = polyproduce;
        this.postProduce = postProduce;
        this.named = named;
        this.classed = classed;
        this.notifyPostProduce = notifyPostProduce;
        this.notifyPostConstruct = notifyPostConstruct;
        this.postConstructListeners = postConstructListeners;
    }

    abstract public Element getOriginElement();

    public void addParameter(InjectableElement paramElm) {
        this.parameters.add(paramElm);
    }

    public void addSupertype(ClassType st) {
        keyTypes.add(st);
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

    public ConditionElement getCondition() {
        return condition;
    }

    public SubstitutionElement getSubstitution() {
        return substitution;
    }

    public Boolean getPolyproduce() {
        return polyproduce;
    }

    public String getNamed() {
        return named;
    }

    public ClassifierType getClassed() {
        return classed;
    }

    public PostProduceElement getPostProduce() {
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

    public List<ClassType> getKeyTypes() {
        return keyTypes;
    }

    @Override
    public String toString() {
        return "FactoryElement{" +
                "suppliedType=" + suppliedType +
                ", factoryMethodBaseName='" + factoryMethodBaseName + '\'' +
                '}';
    }
}
