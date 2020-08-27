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

package colesico.framework.translation.codegen.processor;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.AnnotationType;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.translation.Dictionary;
import colesico.framework.translation.Translation;
import colesico.framework.translation.codegen.generator.BundleGenerator;
import colesico.framework.translation.codegen.generator.DictionaryGenerator;
import colesico.framework.translation.codegen.generator.IocGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import colesico.framework.translation.codegen.model.DictionaryRegistry;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class DictionaryProcessor extends FrameworkAbstractProcessor {


    protected DictionaryGenerator dictionaryGenerator;

    protected DictionaryRegistry dictionaryRegistry;
    protected IocGenerator iocGenerator;
    protected BundleGenerator bundleGenerator;

    public DictionaryProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Dictionary.class};
    }

    @Override
    protected void onInit() {
        this.dictionaryGenerator = new DictionaryGenerator(processingEnv);
        this.iocGenerator = new IocGenerator(processingEnv);
        this.bundleGenerator = new BundleGenerator(processingEnv);
        this.dictionaryRegistry = new DictionaryRegistry(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        dictionaryRegistry.clear();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Dictionary.class);
        for (Element elm : elements) {
            if (elm.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            TypeElement beanDefinitionElement;
            try {
                beanDefinitionElement = (TypeElement) elm;
                logger.debug("Processing dictionary bean: " + beanDefinitionElement.asType().toString());
                DictionaryElement dictionaryBeanElement = parseDictionaryFacade(ClassElement.fromElement(processingEnv, beanDefinitionElement));
                dictionaryRegistry.register(dictionaryBeanElement);
                dictionaryGenerator.generate(dictionaryBeanElement);
            } catch (CodegenException ce) {
                String message = "Error processing dictionary bean '" + elm.toString() + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
            } catch (Exception e) {
                String msg = ExceptionUtils.getRootCauseMessage(e);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        if (!dictionaryRegistry.isEmpty()) {
            iocGenerator.generate(dictionaryRegistry);
            bundleGenerator.generate(dictionaryRegistry);
        }

        return true;
    }

    protected DictionaryElement parseDictionaryFacade(ClassElement dictionaryBeanInterface) {
        DictionaryElement dictionaryBeanElement = new DictionaryElement(dictionaryBeanInterface);


        List<MethodElement> methods = dictionaryBeanInterface.getMethodsFiltered(
                m -> !m.unwrap().getModifiers().contains(Modifier.DEFAULT) &&
                        CodegenUtils.isAssignable(String.class, m.unwrap().getReturnType(), processingEnv)
        );

        for (MethodElement method : methods) {
            dictionaryBeanElement.addTranslationMethod(method);

            // Find translations
            List<AnnotationType> annList = method.getAnnotationTypes();
            for (AnnotationType ann : annList) {
                AnnotationAssist<Translation> translationAnn = ann.asElement().getAnnotation(Translation.class);
                if (translationAnn == null) {
                    continue;
                }
                String localeKey = translationAnn.unwrap().value();
                AnnotationValue value = ann.getValue("value");
                dictionaryBeanElement.addTranslation(method, localeKey, value.getValue().toString());
            }

        }
        return dictionaryBeanElement;
    }

}
