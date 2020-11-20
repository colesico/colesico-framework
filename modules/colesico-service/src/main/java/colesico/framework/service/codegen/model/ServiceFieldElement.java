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

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

/**
 * Represents service proxy filed
 */
public final class ServiceFieldElement {

    /**
     * Parent service ref
     */
    protected ServiceElement parentService;

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

    public ServiceFieldElement inject() {
        this.injectAs = spec.type;
        return this;
    }

    public ServiceFieldElement setInjectAs(TypeName injectingTypenName) {
        this.injectAs = injectingTypenName;
        return this;
    }

    public String getNamed() {
        return named;
    }

    public ServiceFieldElement setNamed(String named) {
        this.named = named;
        return this;
    }

    public TypeName getClassed() {
        return classed;
    }

    public ServiceFieldElement setClassed(TypeName classed) {
        this.classed = classed;
        return this;
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

    public TypeName getInjectAs() {
        return injectAs;
    }

    public ServiceElement getParentService() {
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
