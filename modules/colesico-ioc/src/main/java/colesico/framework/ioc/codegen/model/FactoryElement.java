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

import colesico.framework.ioc.CustomScope;
import colesico.framework.ioc.Unscoped;
import colesico.framework.assist.codegen.CodegenException;

import javax.inject.Singleton;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
abstract public class FactoryElement {

    protected DeclaredType suppliedType;
    protected ScopeElement scope;

    protected Boolean polyproduce;
    protected String named;
    protected String classed;

    protected String factoryMethodBaseName;
    protected int factoryIndex = -1;
    protected final List<InjectableElement> parameters = new ArrayList<>();

    public final String getFactoryMethodName() {
        return factoryMethodBaseName + "Factory" + Integer.toString(factoryIndex);
    }

    public void setFactoryIndex(int factoryIndex) {
        this.factoryIndex = factoryIndex;
    }

    public final DeclaredType getSuppliedType() {
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

    protected ScopeElement extractScope(Element element) {

        Singleton singleton = element.getAnnotation(Singleton.class);
        if (singleton != null) {
            return new ScopeElement(null, ScopeElement.ScopeKind.SINGLETON);
        }

        Unscoped unscoped = element.getAnnotation(Unscoped.class);
        if (unscoped != null) {
            return new ScopeElement(null, ScopeElement.ScopeKind.UNSCOPED);
        }

        ScopeElement result = null;
        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            ScopeElement scopeElm = null;

            CustomScope customScope = am.getAnnotationType().asElement().getAnnotation(CustomScope.class);
            if (customScope != null) {
                try {
                    customScope.value();
                } catch (MirroredTypeException mte) {
                    scopeElm = new ScopeElement(mte.getTypeMirror(), ScopeElement.ScopeKind.CUSTOM);
                }
            }

            if (scopeElm != null) {
                if (result == null) {
                    result = scopeElm;
                } else {
                    throw CodegenException.of().message("Ambiguous scope definition").element(element).create();
                }
            }
        }
        return result;
    }
}
