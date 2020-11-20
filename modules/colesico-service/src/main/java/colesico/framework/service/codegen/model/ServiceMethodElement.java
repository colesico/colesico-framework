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

import colesico.framework.assist.codegen.model.MethodElement;

import java.util.*;

/**
 * Represent the method of a service
 *
 * @author Vladlen Larionov
 */
public final class ServiceMethodElement {

    /**
     * Parent service
     */
    protected ServiceElement parentService;

    /**
     * Origin service method reference
     */
    private final MethodElement originMethod;

    /**
     * Indicates that the method is not tele-method
     */
    private final boolean isLocal;

    /**
     * Method is post construct listener
     */
    private final boolean isPostConstructListener;

    private final Map<String, List<InterceptionElement>> interceptionsByPhase;

    private InterceptionElement superMethodInterception;

    /**
     * Common purpose properties
     */
    private final Map<Class, Object> properties;

    public ServiceMethodElement(MethodElement originMethod, boolean isLocal, boolean isPostConstructListener) {
        this.originMethod = originMethod;
        this.isLocal = isLocal;
        this.isPostConstructListener = isPostConstructListener;
        this.interceptionsByPhase = new HashMap<>();
        this.properties = new HashMap();
    }

    public <P> P getProperty(Class<P> propertyClass) {
        return (P) properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }

    public final void addInterception(String interceptionPhase, InterceptionElement interception) {
        List<InterceptionElement> phaseInterceptions = this.interceptionsByPhase.computeIfAbsent(interceptionPhase, k -> new ArrayList<>());
        phaseInterceptions.add(interception);
    }

    public InterceptionElement getSuperMethodInterception() {
        return superMethodInterception;
    }

    public void setSuperMethodInterception(InterceptionElement interception) {
        this.superMethodInterception = interception;
    }

    public final String getName() {
        return originMethod.getName();
    }

    public final MethodElement getOriginMethod() {
        return originMethod;
    }

    public final boolean isLocal() {
        return isLocal;
    }

    public boolean isPostConstructListener() {
        return isPostConstructListener;
    }

    public final List<InterceptionElement> getPhaseInterceptions(String interceptionPhase) {
        List<InterceptionElement> phaseInterceptions = interceptionsByPhase.get(interceptionPhase);
        if (phaseInterceptions == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(phaseInterceptions);
    }

    public ServiceElement getParentService() {
        return parentService;
    }

    @Override
    public String toString() {
        return "ServiceMethodElement{" +
                "originMethod=" + originMethod +
                '}';
    }
}
