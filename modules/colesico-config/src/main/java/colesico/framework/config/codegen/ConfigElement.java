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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.config.Default;
import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.assist.codegen.CodegenUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Vladlen Larionov
 */
public class ConfigElement {
    /**
     * Configuration implementation class
     */
    private final TypeElement implementation;

    /**
     * Configuration prototype class
     */
    private final TypeElement prototype;

    /**
     * Configuration rank
     */
    private final String rank;

    /**
     * Type of configuration
     */
    private final ConfigModel model;

    /**
     * Configurable target for this configuration
     */
    private final TypeMirror target;

    private final boolean defaultMessage;

    public ConfigElement(TypeElement implementation, TypeElement prototype, String rank) {
        this.implementation = implementation;
        this.prototype = prototype;
        this.rank = rank;

        ConfigPrototype prototypeAnn = prototype.getAnnotation(ConfigPrototype.class);
        this.model = prototypeAnn.model();
        this.target = CodegenUtils.getAnnotationValueTypeMirror(prototypeAnn, a -> a.target());

        Default classedDefaultAnn = implementation.getAnnotation(Default.class);

        if (classedDefaultAnn != null) {
            if (!ConfigModel.MESSAGE.equals(model)) {
                throw CodegenException.of().message("@" + Default.class.getSimpleName() +
                        " annotation can be applied only to " + ConfigModel.MESSAGE.name() + " configuration model").build();
            }
            defaultMessage = true;
        } else {
            defaultMessage = false;
        }
    }

    public TypeElement getImplementation() {
        return implementation;
    }

    public ConfigModel getModel() {
        return model;
    }

    public TypeElement getPrototype() {
        return prototype;
    }

    public String getRank() {
        return rank;
    }

    public TypeMirror getTarget() {
        return target;
    }

    public boolean getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return "ConfigElement{" +
                "implementation=" + implementation +
                ", prototype=" + prototype +
                ", rank='" + rank + '\'' +
                ", model=" + model +
                ", target=" + target +
                ", classedDefault=" + defaultMessage +
                '}';
    }
}
