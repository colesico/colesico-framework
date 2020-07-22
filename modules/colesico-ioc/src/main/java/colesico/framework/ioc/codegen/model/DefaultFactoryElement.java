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

package colesico.framework.ioc.codegen.model;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.ioc.production.Produce;

import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class DefaultFactoryElement extends FactoryElement {

    private final MethodElement constructor;
    private final AnnotationAssist<Produce> produce;

    public DefaultFactoryElement(ClassType suppliedType,
                                 String factoryMethodBaseName,
                                 ScopeElement scope,
                                 ConditionElement condition,
                                 Boolean polyproduce,
                                 String named,
                                 ClassType classed,
                                 boolean notifyPostProduce,
                                 boolean notifyPostConstruct,
                                 List<MethodElement> postConstructListeners,
                                 MethodElement constructor,
                                 AnnotationAssist<Produce> produce
    ) {
        super(suppliedType,
                factoryMethodBaseName,
                scope,
                condition,
                null,
                polyproduce,
                null,
                named,
                classed,
                notifyPostProduce,
                notifyPostConstruct,
                postConstructListeners);
        this.constructor = constructor;
        this.produce = produce;
    }

    public MethodElement getConstructor() {
        return constructor;
    }

    public AnnotationAssist<Produce> getProduce() {
        return produce;
    }
}
