/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;

/**
 * Represents service proxy custom filed
 */
public final class ServiceFieldElement {

    /**
     * Parent service ref
     */
    ServiceElement parentService;

    /**
     * Field spec
     */
    private final FieldSpec spec;

    /**
     * Inject field as given type
     */
    private TypeName injectAs;

    /**
     * Inject with @Named
     */
    private String named;

    /**
     * Inject with @Classed
     */
    private TypeName classed;

    public ServiceFieldElement(FieldSpec spec) {
        this.spec = spec;
    }

    /**
     *  Mark field to be injected
     */
    public ServiceFieldElement inject() {
        this.injectAs = spec.type();
        return this;
    }

    public ServiceFieldElement setInjectAs(TypeName injectingTypeName) {
        this.injectAs = injectingTypeName;
        return this;
    }

    public String named() {
        return named;
    }

    public ServiceFieldElement setNamed(String named) {
        this.named = named;
        return this;
    }

    public TypeName classed() {
        return classed;
    }

    public ServiceFieldElement setClassed(TypeName classed) {
        this.classed = classed;
        return this;
    }

    public String name() {
        return spec.name();
    }

    public String typeName() {
        return spec.type().toString();
    }

    public FieldSpec spec() {
        return spec;
    }

    public TypeName injectAs() {
        return injectAs;
    }

    public ServiceElement parentService() {
        return parentService;
    }

    @Override
    public String toString() {
        return "ServiceFieldElement{" +
                "spec=" + spec +
                ", injectAs=" + injectAs +
                ", named='" + named + '\'' +
                ", classed=" + classed +
                '}';
    }
}
