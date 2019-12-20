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

package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import colesico.framework.translation.codegen.model.DictionaryRegistry;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.List;
import java.util.Map;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String T9N_PRODUCER = "T9nProducer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }


    private void generateIocProduccer(String packageName, String producerClassSimpleName, List<DictionaryElement> dictionaryElements) {
        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), processingEnv);
        logger.debug("Generating t9n producer: " + producerGenerator.getProducerClassFilePath());

        int i = 0;
        for (DictionaryElement dbe : dictionaryElements) {
            TypeName implTypeName = ClassName.bestGuess(packageName + '.' + dbe.getImplClassSimpleName());
            producerGenerator.addProduceAnnotation(implTypeName);

            String methodName = "get" + dbe.getOriginBean().getSimpleName() + i;
            TypeName retTypeName = TypeName.get(dbe.getOriginBean().asType());
            producerGenerator.addImplementMethod(methodName, retTypeName, implTypeName);
            i++;
        }

        producerGenerator.generate();
    }

    public void generate(DictionaryRegistry dictionaryRegistry) {
        for (Map.Entry<String, List<DictionaryElement>> entry : dictionaryRegistry.getByPackageMap().entrySet()) {
            String packageName = entry.getKey();
            List<DictionaryElement> dictElements = entry.getValue();
            if (getCodegenMode().isOptimized()) {
                generateIocProduccer(packageName, T9N_PRODUCER, dictElements);
            } else {
                for (DictionaryElement dictElm : dictElements) {
                    String t9nProducerName = dictElm.getOriginBean().getSimpleName() + T9N_PRODUCER;
                    generateIocProduccer(packageName, t9nProducerName, List.of(dictElm));
                }
            }
        }
    }

}
