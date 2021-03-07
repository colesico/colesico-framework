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

package colesico.framework.introspection.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.introspection.Introspect;
import colesico.framework.introspection.codegen.model.IntrospectedElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class IntrospectionProcessor extends FrameworkAbstractProcessor {

    protected IntrospectionParser parser;

    public IntrospectionProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Introspect.class};
    }

    @Override
    protected void onInit() {
        this.parser = new IntrospectionParser(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("Start introspection processing...");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Introspect.class);
        for (Element elm : elements) {
            if (elm.getKind() != ElementKind.CLASS && elm.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            TypeElement typeElement;
            try {
                typeElement = (TypeElement) elm;
                ClassElement classElement = ClassElement.fromElement(processingEnv, typeElement);
                logger.debug("Processing introspection: " + classElement.getName());
                IntrospectedElement parsedElement = parser.parse(classElement);
                // TODO: generate code
            } catch (CodegenException ce) {
                String message = "Error processing introspected type '" + elm.toString() + "': " + ce.getMessage();
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


        return true;
    }

}
