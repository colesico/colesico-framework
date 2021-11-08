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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.config.ConfigModel;
import colesico.framework.config.DefaultConfig;
import colesico.framework.ioc.conditional.Substitution;

import javax.lang.model.type.TypeMirror;

/**
 * @author Vladlen Larionov
 */
public class ConfigElement {

    /**
     * Configuration origin class element (annotated with {@link colesico.framework.config.Config})
     */
    private final ClassElement originClass;

    /**
     * Configuration prototype class
     */
    private final ClassElement prototype;

    /**
     * Configuration condition
     */
    private final ClassType condition;

    /**
     * Configuration substitution
     */
    private final Substitution substitution;

    /**
     * Type of configuration
     */
    private final ConfigModel model;

    /**
     * Scoped annotation type
     * @see javax.inject.Singleton
     * @see colesico.framework.ioc.scope.Unscoped
     * @see colesico.framework.ioc.scope.CustomScope
     */
    private final ConfigScopedElement scoped;

    /**
     * Configurable target for this configuration
     */
    private final ClassElement target;

    /**
     * Configuration source definition
     */
    private ConfigSourceElement source;

    /**
     * @see DefaultConfig
     */
    private final boolean defaultMessage;

    /**
     * If @Classed annotation is defined on configuration instance
     */
    private final TypeMirror classedQualifier;

    /**
     * If @Named annotation is defined on configuration instance
     */
    private final String namedQualifier;

    public ConfigElement(ClassElement originClass,
                         ClassElement prototype,
                         ClassType condition,
                         Substitution substitution,
                         ConfigModel model,
                         ConfigScopedElement scoped,
                         ClassElement target,
                         boolean defaultMessage,
                         TypeMirror classedQualifier,
                         String namedQualifier) {

        this.originClass = originClass;
        this.prototype = prototype;
        this.condition = condition;
        this.substitution = substitution;
        this.model = model;
        this.scoped = scoped;
        this.target = target;
        this.defaultMessage = defaultMessage;
        this.classedQualifier = classedQualifier;
        this.namedQualifier = namedQualifier;
    }

    public ClassElement getOriginClass() {
        return originClass;
    }

    public ClassElement getPrototype() {
        return prototype;
    }

    public ClassType getCondition() {
        return condition;
    }

    public Substitution getSubstitution() {
        return substitution;
    }

    public ConfigModel getModel() {
        return model;
    }

    public ConfigScopedElement getScoped() {
        return scoped;
    }

    public ClassElement getTarget() {
        return target;
    }

    public boolean getDefaultMessage() {
        return defaultMessage;
    }

    public TypeMirror getClassedQualifier() {
        return classedQualifier;
    }

    public String getNamedQualifier() {
        return namedQualifier;
    }

    public ConfigSourceElement getSource() {
        return source;
    }

    public void setSource(ConfigSourceElement source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ConfigElement{" +
                "implementation=" + originClass +
                ", prototype=" + prototype +
                ", condition=" + condition +
                ", substitution=" + substitution +
                ", model=" + model +
                ", target=" + target +
                ", source=" + source +
                ", defaultMessage=" + defaultMessage +
                ", classedQualifier=" + classedQualifier +
                ", namedQualifier='" + namedQualifier + '\'' +
                '}';
    }
}
