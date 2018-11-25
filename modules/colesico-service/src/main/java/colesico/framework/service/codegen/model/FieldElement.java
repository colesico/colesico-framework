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

package colesico.framework.service.codegen.model;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

/**
 * @author Vladlen Larionov
 */
public final class FieldElement {

    protected ServiceElement parentService;

    private final FieldSpec spec;
    private final boolean inject;
    private final TypeName injectionClass;

    public FieldElement(FieldSpec spec, boolean inject, TypeName injectionClass) {
        this.spec = spec;
        this.inject = inject;
        if (injectionClass!=null) {
            this.injectionClass = injectionClass;
        } else {
            this.injectionClass = spec.type;
        }
    }

    public String getName() {
        return spec.name;
    }

    public String getTypeName() {
        return spec.type.toString();
    }

    public FieldSpec getSpec() {
        return spec;
    }

    public boolean isInject() {
        return inject;
    }

    public TypeName getInjectionClass() {
        return injectionClass;
    }

    public ServiceElement getParentService() {
        return parentService;
    }

    @Override
    public String toString() {
        return "FieldElement{" +
                "spec=" + spec +
                ", inject=" + inject +
                ", injectionClass=" + injectionClass +
                '}';
    }
}
