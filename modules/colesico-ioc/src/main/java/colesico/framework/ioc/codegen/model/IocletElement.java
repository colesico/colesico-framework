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

import colesico.framework.assist.codegen.model.ClassElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class IocletElement {

    public static final String PRODUCER_SUFFIX = "Producer";
    public static final String IOCLET_SUFFIX = "Ioclet";

    private final ClassElement originProducer;
    private final String iocletId;

    private final String iocletClassSimpleName;
    private final String iocletPackageName;


    private final List<FactoryElement> factories = new ArrayList<>();

    public IocletElement(ClassElement originProducer,
                         String iocletId,
                         String iocletClassSimpleName, String iocletPackageName) {
        this.originProducer = originProducer;
        this.iocletId = iocletId;
        this.iocletClassSimpleName = iocletClassSimpleName;
        this.iocletPackageName = iocletPackageName;
    }

    public void addFactory(FactoryElement factoryElement) {
        factoryElement.setFactoryIndex(factories.size());
        factories.add(factoryElement);
    }

    public List<FactoryElement> getFactories() {
        return factories;
    }

    public String getIocletId() {
        return iocletId;
    }

    public ClassElement getOriginProducer() {
        return originProducer;
    }

    public String getIocletClassName() {
        return iocletPackageName + '.' + iocletClassSimpleName;
    }

    public String getIocletClassSimpleName() {
        return iocletClassSimpleName;
    }

    public String getIocletPackageName() {
        return iocletPackageName;
    }

    @Override
    public String toString() {
        return "IocletElement{" +
                "originProducer=" + originProducer +
                ", iocletId='" + iocletId + '\'' +
                '}';
    }
}
