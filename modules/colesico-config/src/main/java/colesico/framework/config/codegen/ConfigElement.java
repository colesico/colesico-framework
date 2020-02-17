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
     * Configuration implementation class
     */
    private final ClassElement implementation;

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

    public ConfigElement(ClassElement implementation,
                         ClassElement prototype,
                         ClassType condition,
                         Substitution substitution,
                         ConfigModel model,
                         ClassElement target,
                         boolean defaultMessage,
                         TypeMirror classedQualifier,
                         String namedQualifier) {

        this.implementation = implementation;
        this.prototype = prototype;
        this.condition = condition;
        this.substitution = substitution;
        this.model = model;
        this.target = target;
        this.defaultMessage = defaultMessage;
        this.classedQualifier = classedQualifier;
        this.namedQualifier = namedQualifier;
    }

    public ClassElement getImplementation() {
        return implementation;
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
                "implementation=" + implementation +
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
