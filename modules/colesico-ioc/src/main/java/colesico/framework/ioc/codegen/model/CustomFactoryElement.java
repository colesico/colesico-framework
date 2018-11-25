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

import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polyproduce;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @author Vladlen Larionov
 */
public class CustomFactoryElement extends FactoryElement {

    private final ExecutableElement producerMethod;

    public CustomFactoryElement(ExecutableElement producerMethod) {
        this.producerMethod = producerMethod;
        try {
            this.suppliedType = (DeclaredType) producerMethod.getReturnType();
        } catch (Exception e){
            throw CodegenException.of().message("Unable to determine producer method return type for "+producerMethod)
                    .element(producerMethod)
                    .create();
        }
        this.factoryMethodBaseName = this.producerMethod.getSimpleName().toString();

        // get scope
        ScopeElement scopeElm = extractScope(producerMethod);
        if (scopeElm == null) {
            scopeElm = extractScope(suppliedType.asElement());
            if (scopeElm == null) {
                scopeElm = new ScopeElement(null, ScopeElement.ScopeKind.UNSCOPED);
            }
        }
        this.scope = scopeElm;

        for (VariableElement param : producerMethod.getParameters()) {
            this.parameters.add(new InjectableElement(this,param));
        }

        this.polyproduce = producerMethod.getAnnotation(Polyproduce.class)!=null;

        Named namedAnn = producerMethod.getAnnotation(Named.class);
        if (namedAnn != null) {
            this.named = namedAnn.value();
        }


        Classed classedAnn = producerMethod.getAnnotation(Classed.class);
        if (classedAnn != null) {
            TypeMirror classifier = CodegenUtils.getAnnotationValueTypeMirror(classedAnn, a -> a.value());
            this.classed = classifier.toString();
        }

        if (StringUtils.isNotEmpty(this.named) && this.classed!=null){
            CodegenException.of().message("Ambiguous injection qualifiers").element(producerMethod).create();
        }
    }

    public ExecutableElement getProducerMethod() {
        return producerMethod;
    }



}
