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

import colesico.framework.ioc.Producer;
import colesico.framework.assist.codegen.CodegenUtils;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
public class IocletElement {

    public static final String PRODUCER_SUFFIX = "Producer";
    public static final String IOCLET_SUFFIX = "Ioclet";

    private final TypeElement originProducer;

    private final String classSimpleName;
    private final String packageName;
    private final String producerId;
    private final String rank;

    private final List<FactoryElement> factories;

    public IocletElement(TypeElement originProducer) {
        this.originProducer = originProducer;

        Producer producerAnn = originProducer.getAnnotation(Producer.class);
        this.rank = producerAnn.value();

        String producerClassSimpleName = originProducer.getSimpleName().toString();
        if (producerClassSimpleName.endsWith(PRODUCER_SUFFIX)) {
            producerClassSimpleName = producerClassSimpleName.substring(0, producerClassSimpleName.length() - PRODUCER_SUFFIX.length());
        }
        this.classSimpleName = producerClassSimpleName + IOCLET_SUFFIX;
        this.packageName = CodegenUtils.getPackageName(originProducer);


        this.producerId = originProducer.asType().toString();

        this.factories = new ArrayList<>();
    }

    public void addFactory(FactoryElement factoryElement) {
        factoryElement.setFactoryIndex(factories.size());
        factories.add(factoryElement);
    }

    public String getRank() {
        return rank;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return packageName + '.' + classSimpleName;
    }

    public List<FactoryElement> getFactories() {
        return factories;
    }

    public String getProducerId() {
        return producerId;
    }

    public TypeElement getOriginProducer() {
        return originProducer;
    }


    @Override
    public String toString() {
        return "IocletElement{" +
                "originProducer=" + originProducer +
                ", classSimpleName='" + classSimpleName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", producerName='" + producerId + '\'' +
                ", rank='" + rank + '\'' +
                ", factories=" + factories +
                '}';
    }
}
