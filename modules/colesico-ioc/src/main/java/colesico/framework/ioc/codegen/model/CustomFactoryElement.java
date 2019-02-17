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
import colesico.framework.assist.codegen.model.MethodElement;

/**
 * @author Vladlen Larionov
 */
public class CustomFactoryElement extends FactoryElement {

    private final MethodElement producerMethod;

    public CustomFactoryElement(ClassType suppliedType, String factoryMethodBaseName, ScopeElement scope, Boolean polyproduce, String named, String classed, MethodElement producerMethod) {
        super(suppliedType, factoryMethodBaseName, scope, polyproduce, named, classed);
        this.producerMethod = producerMethod;
    }

    public MethodElement getProducerMethod() {
        return producerMethod;
    }

    @Override
    public String toString() {
        return "CustomFactoryElement{" +
                "producerMethod=" + producerMethod +
                ", suppliedType=" + suppliedType +
                ", scope=" + scope +
                ", polyproduce=" + polyproduce +
                ", named='" + named + '\'' +
                ", classed='" + classed + '\'' +
                '}';
    }
}
