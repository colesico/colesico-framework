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

package colesico.framework.jdbirec.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.jdbirec.RecordKitConfig;
import colesico.framework.jdbirec.codegen.generator.RecordKitGenerator;
import colesico.framework.jdbirec.codegen.model.ViewSetElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

public class RecordProcessor extends FrameworkAbstractProcessor {

    private RecordKitParser recordKitParser;
    private RecordKitGenerator recordHelperGenerator;

    public RecordProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{RecordKitConfig.class};
    }

    @Override
    protected void onInit() {
        recordKitParser = new RecordKitParser(processingEnv);
        recordHelperGenerator = new RecordKitGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element elm : roundEnv.getElementsAnnotatedWith(RecordKitConfig.class)) {
            if (!(elm.getKind() == ElementKind.INTERFACE)) {
                throw CodegenException.of().element(elm).message("Not an interface").build();
            }
            TypeElement recordKitClass;
            try {
                recordKitClass = (TypeElement) elm;
                logger.debug("Processing record kit class: " + recordKitClass.getSimpleName());
                ViewSetElement views = recordKitParser.parse(new ClassElement(processingEnv, recordKitClass));
                recordHelperGenerator.generate(views);
            } catch (CodegenException ce) {
                String message = "Error processing class '" + elm.toString() + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
                String msg = ExceptionUtils.getRootCauseMessage(e);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }
}


