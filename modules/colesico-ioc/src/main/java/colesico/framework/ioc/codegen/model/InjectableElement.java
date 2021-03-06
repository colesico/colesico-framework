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
import colesico.framework.assist.codegen.model.ParameterElement;

/**
 * The parameter to be injected
 *
 * @author Vladlen Larionov
 */
public class InjectableElement {

    private final FactoryElement parentFactory;
    private final ParameterElement originParameter;

    // type to be injected
    private final ClassType injectedType;

    private final InjectionKind injectionKind;
    private final MessageKind messageKind;

    // Optional injection
    private final boolean optional;

    //  @Named value
    private final String named;

    //  @Classed value as class names
    private final ClassifierType classed;

    public InjectableElement(FactoryElement parentFactory,
                             ParameterElement originParameter,
                             ClassType injectedType,
                             InjectionKind injectionKind,
                             MessageKind messageKind,
                             Boolean optional,
                             String named,
                             ClassifierType classed) {

        this.parentFactory = parentFactory;
        this.originParameter = originParameter;
        this.injectedType = injectedType;
        this.injectionKind = injectionKind;
        this.messageKind = messageKind;
        this.optional = optional;
        this.named = named;
        this.classed = classed;
    }

    public FactoryElement getParentFactory() {
        return parentFactory;
    }

    public ParameterElement getOriginParameter() {
        return originParameter;
    }

    public ClassType getInjectedType() {
        return injectedType;
    }

    public InjectionKind getInjectionKind() {
        return injectionKind;
    }

    public MessageKind getMessageKind() {
        return messageKind;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getNamed() {
        return named;
    }

    public ClassifierType getClassed() {
        return classed;
    }

    public enum InjectionKind {
        MESSAGE,
        INSTANCE,
        PROVIDER,
        SUPPLIER,
        POLYSUPPLIER
    }

    public enum MessageKind {
        OUTER_MESSAGE,
        INJECTION_POINT,
    }
}
