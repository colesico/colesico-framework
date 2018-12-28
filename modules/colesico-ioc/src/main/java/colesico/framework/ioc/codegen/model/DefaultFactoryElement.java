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

import colesico.framework.ioc.Produce;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @author Vladlen Larionov
 */
public class DefaultFactoryElement extends FactoryElement {

    private final Produce produce;
    private final ExecutableElement constructor;

    public DefaultFactoryElement(TypeElement producerElement, Produce produceAnn, ExecutableElement constructorElement) {
        this.constructor = constructorElement;
        this.suppliedType = (DeclaredType) constructorElement.getEnclosingElement().asType();
        this.factoryMethodBaseName = "get" + suppliedType.asElement().getSimpleName();

        this.produce = produceAnn;

        this.polyproduce = produceAnn.polyproduce();
        if (!polyproduce) {
            //TODO: check suppliedType annotation @Polyproduce
        }


        ScopeElement sce = extractScope(this.suppliedType.asElement());
        if (sce == null) {
            sce = new ScopeElement(null, ScopeElement.ScopeKind.UNSCOPED);
        }
        scope = sce;

        for (VariableElement param : constructorElement.getParameters()) {
            this.parameters.add(new InjectableElement(this,param));
        }

        if (StringUtils.isNotEmpty(produceAnn.named())) {
            this.named = produceAnn.named();
        } else {
            //TODO: check suppliedType annotation @Named
        }

        TypeMirror classifier = CodegenUtils.getAnnotationValueTypeMirror(produceAnn, a -> a.classed());
        if (!Class.class.getName().equals(classifier.toString())) {
            this.classed = classifier.toString();
        } else {
            //TODO: check suppliedType annotation @Classed
        }

        if (StringUtils.isNotEmpty(this.named) && this.classed != null) {
            CodegenException.of().message("Ambiguous injection qualifiers for " +
                    constructorElement.getReturnType().toString()
            ).element(producerElement).build();
        }
    }

    public ExecutableElement getConstructor() {
        return constructor;
    }

    public Produce getProduce() {
        return produce;
    }
}
