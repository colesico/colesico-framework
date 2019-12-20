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

import colesico.framework.assist.codegen.model.AnnotationElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.ioc.Produce;

import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class DefaultFactoryElement extends FactoryElement {

    private final MethodElement constructor;
    private final AnnotationElement<Produce> produce;

    public DefaultFactoryElement(ClassType suppliedType,
                                 String factoryMethodBaseName,
                                 ScopeElement scope,
                                 Boolean polyproduce,
                                 String named,
                                 ClassType classed,
                                 boolean notifyPostProduce,
                                 boolean notifyPostConstruct,
                                 List<MethodElement> postConstructListeners,
                                 MethodElement constructor,
                                 AnnotationElement<Produce> produce
    ) {
        super(suppliedType,
            factoryMethodBaseName,
            scope,
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

    public AnnotationElement<Produce> getProduce() {
        return produce;
    }
}
