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

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.ioc.*;

import javax.inject.Named;
import javax.inject.Provider;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * The parameter to be injected
 *
 * @author Vladlen Larionov
 */
public class InjectableElement {
    private final FactoryElement parentFactory;
    private final VariableElement originParameter;

    // type  to be injected
    private final DeclaredType injectedType;
    private final InjectionKind injectionKind;
    private final MessageKind messageKind;

    //  @Named value
    private final String named;
    //  @Classed value as class names
    private final String classed;


    public InjectableElement(FactoryElement parentFactory, VariableElement parameter) {
        this.parentFactory = parentFactory;
        this.originParameter = parameter;
        DeclaredType declaredType = (DeclaredType) parameter.asType();
        String parameterClassName = CodegenUtils.getClassName((TypeElement) declaredType.asElement());

        // Injection kind detection
        Message messageAnn = parameter.getAnnotation(Message.class);
        if (messageAnn != null) {
            this.injectedType = declaredType;
            this.injectionKind = InjectionKind.MESSAGE;
        } else {
            if (parameterClassName.equals(Supplier.class.getName())
                    || parameterClassName.equals(Provider.class.getName())
                    || parameterClassName.equals(Polysupplier.class.getName())) {
                List<? extends TypeMirror> generics = declaredType.getTypeArguments();
                if (generics.isEmpty()) {
                    throw CodegenException.of().message("Unable to determine injecting type").element(parameter).create();
                }
                this.injectedType = ((DeclaredType) generics.get(0));
                if (parameterClassName.equals(Provider.class.getName())) {
                    this.injectionKind = InjectionKind.PROVIDER;
                } else if (parameterClassName.equals(Polysupplier.class.getName())) {
                    this.injectionKind = InjectionKind.POLYSUPPLIER;
                } else {
                    this.injectionKind = InjectionKind.SUPPLIER;
                }
            } else {
                this.injectedType = declaredType;
                this.injectionKind = InjectionKind.INSTANCE;
            }
        }

        // Messaging
        Contextual inherentAnn = parameter.getAnnotation(Contextual.class);
        this.messageKind = inherentAnn != null ? MessageKind.INJECTION_POINT : MessageKind.OUTER_MESSAGE;

        // Extra key types
        Named namedAnn = parameter.getAnnotation(Named.class);
        if (namedAnn != null) {
            this.named = namedAnn.value();
            // TODO: check  injectionKind
        } else {
            this.named = null;
        }

        Classed classedAnn = parameter.getAnnotation(Classed.class);
        if (classedAnn != null) {
            TypeMirror classifier = CodegenUtils.getAnnotationValueTypeMirror(classedAnn, a -> a.value());
            this.classed = classifier.toString();
            // TODO: check  injectionKind
        } else {
            this.classed = null;
        }

    }

    public FactoryElement getParentFactory() {
        return parentFactory;
    }

    public VariableElement getOriginParameter() {
        return originParameter;
    }

    public DeclaredType getInjectedType() {
        return injectedType;
    }

    public InjectionKind getInjectionKind() {
        return injectionKind;
    }

    public MessageKind getMessageKind() {
        return messageKind;
    }

    public String getNamed() {
        return named;
    }

    public String getClassed() {
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
