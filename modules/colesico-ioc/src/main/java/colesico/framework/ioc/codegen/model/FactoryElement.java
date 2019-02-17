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

package colesico.framework.ioc.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

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
    protected final String named;
    protected final String classed;

    protected final List<InjectableElement> parameters = new ArrayList<>();

    protected int factoryIndex = -1;

    public FactoryElement(ClassType suppliedType, String factoryMethodBaseName, ScopeElement scope, Boolean polyproduce, String named, String classed) {
        this.suppliedType = suppliedType;
        this.factoryMethodBaseName = factoryMethodBaseName;
        this.scope = scope;
        this.polyproduce = polyproduce;
        this.named = named;
        this.classed = classed;
    }

    public final String getFactoryMethodName() {
        return factoryMethodBaseName + "Factory" + Integer.toString(factoryIndex);
    }

    public void setFactoryIndex(int factoryIndex) {
        this.factoryIndex = factoryIndex;
    }

    public final ClassType getSuppliedType() {
        return suppliedType;
    }

    public final ScopeElement getScope() {
        return scope;
    }

    public Boolean getPolyproduce() {
        return polyproduce;
    }

    public final List<InjectableElement> getParameters() {
        return parameters;
    }

    public String getNamed() {
        return named;
    }

    public String getClassed() {
        return classed;
    }

    public void addParameter(InjectableElement parameter) {
        parameters.add(parameter);
    }

}
