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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.config.Config;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public class ConfigProcessor extends FrameworkAbstractProcessor {

    protected IocGenerator iocGenerator;
    protected BagGenerator bagGenerator;
    protected ConfigParser configParser;

    public ConfigProcessor() {
        super();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{Config.class};
    }

    @Override
    protected void onInit() {
        this.iocGenerator = new IocGenerator(processingEnv);
        this.bagGenerator = new BagGenerator(processingEnv);
        this.configParser = new ConfigParser(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("Start configurations processing...");
        List<ConfigElement> configElements = new ArrayList<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Config.class);
        for (Element elm : elements) {
            if (elm.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement configClassElement;
            try {
                configClassElement = (TypeElement) elm;
                ClassElement classElement = ClassElement.fromElement(processingEnv, configClassElement);
                logger.debug("Processing configuration: " + classElement.getName());
                ConfigElement ce = configParser.parse(classElement);
                configElements.add(ce);
            } catch (CodegenException ce) {
                String message = "Error processing configuration class '" + elm.toString() + "': " + ce.getMessage();
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

        if (!configElements.isEmpty()) {
            try {
                bagGenerator.generate(configElements);
                iocGenerator.generate(configElements);
            } catch (Exception e){
                e.printStackTrace();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ExceptionUtils.getRootCauseMessage(e));
            }
        }

        return true;
    }

}
