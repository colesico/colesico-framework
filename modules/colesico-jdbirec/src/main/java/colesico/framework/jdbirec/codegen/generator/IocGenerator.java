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

package colesico.framework.jdbirec.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.production.Produce;
import colesico.framework.jdbirec.codegen.model.RecordKitElement;
import colesico.framework.jdbirec.codegen.model.RecordViewElement;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String PRODUCER_SUFFIX = "Producer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(RecordKitElement recordKit) {

        String packageName = recordKit.getOriginClass().getPackageName();
        String producerClassSimpleName = recordKit.getOriginClass().getSimpleName() + PRODUCER_SUFFIX;
        ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), processingEnv);

        for (RecordViewElement view : recordKit.getRecord().getViews()) {
            logger.debug("Generating JDBI Rec producer: " + producerGenerator.getProducerClassFilePath());
            String implPackageStr = view.getType().asClassElement().getPackageName();
            String implClassStr = RecordKitGeneratorUtils.buildRecordKitInstanceClassName(view);
            ClassName implClassName = ClassName.bestGuess(implPackageStr + "." + implClassStr);
            AnnotationSpec.Builder produceAnn = producerGenerator.addProduceAnnotation(implClassName);
            if (!view.isDefaultView()) {
                produceAnn.addMember(Produce.NAMED_METHOD, "$S", view.getName());
            }

            TypeName recKitType = TypeName.get(recordKit.getOriginClass().asClassType().unwrap());
            produceAnn.addMember(Produce.KEY_TYPE_METHOD, "$T.class", recKitType);

        }

        producerGenerator.generate();
    }


}
