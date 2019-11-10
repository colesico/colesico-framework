/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.ioc.codegen.generator;

import colesico.framework.assist.codegen.SPIUtils;
import colesico.framework.ioc.codegen.model.IocletElement;
import colesico.framework.ioc.ioclet.Ioclet;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SPIGenerator {

    private final ProcessingEnvironment processingEnv;

    public SPIGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void generateSPIFile(Collection<IocletElement> ioclets) {
        Set<String> iocletClassNames = new HashSet<>();
        for (IocletElement ie : ioclets) {
            iocletClassNames.add(ie.getIocletClassName());
        }
        SPIUtils.addService(Ioclet.class.getCanonicalName(), iocletClassNames, processingEnv);
    }
}
